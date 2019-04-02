import React from "react";
import {displayLoginDialog} from "../actions/login";
import {sendMessage} from "../actions/screen";
import {connect} from "react-redux";
// import styled from 'styled-components';
import PropTypes from 'prop-types';

import "./ChatCardComponent.css"

function formatDate(date) {
  let dayOfMonth = date.getDate();
  let month = date.getMonth() + 1;
  let year = date.getFullYear();
  let hour = date.getHours();
  let minutes = date.getMinutes();
  let diffMs = new Date() - date;
  let diffSec = Math.round(diffMs / 1000);
  let diffMin = diffSec / 60;
  let diffHour = diffMin / 60;

  year = year.toString().slice(-2);
  month = month < 10 ? '0' + month : month;
  dayOfMonth = dayOfMonth < 10 ? '0' + dayOfMonth : dayOfMonth;

  if (diffSec < 1) {
    return 'right now';
  } else if (diffMin < 1) {
    return `${diffSec.toFixed(0)} sec. ago`
  } else if (diffHour < 1) {
    return `${diffMin.toFixed(0)} min. ago`
  } else {
    return `${dayOfMonth}.${month}.${year} ${hour}:${minutes}`
  }
}

class ChatElement extends React.Component {
  render() {
    let side = "";
    if (this.props.userIsSender) side = "self";
    else if (this.props.userIsReceiver) side = "other";

    return (
      <li className={side}>
        <div className="avatar">
        </div>
        <div className="messages">
          <p>{this.props.content}</p>
          <span>{this.props.userIsReceiver ? this.props.sender + " • " : ""} {formatDate(new Date(this.props.timeSent * 1000))}</span>
        </div>
      </li>
    );
  }
}

ChatElement.propType = {
  userIsSender: PropTypes.string,
  userIsReceiver: PropTypes.string,
  content: PropTypes.string,
};

class ChatCardComponent extends React.Component {
  constructor(props) {
    super(props);
    this.lastElementLoc = React.createRef();
    this.content = React.createRef();

    this.colorArray = ["#4b5dbd", "#44a1f4", "#4ab2f6", "#4dc3da", "#3a9f93", "#5eb659", "#826051", "#6d8896",
      "#a8a8a7", "#f36624", "#ef543b", "#e6396c", "#a337b9", "#7247c0"];
    this.state = {
      isChatOpened: false,
    };

    this.toggleChatMode = () => {
      if (!this.state.isChatOpened) {         // just opened, show the last message
        this.goToLastChat();
      }
      this.setState(Object.assign({}, this.state, {isChatOpened: !this.state.isChatOpened}))
    };
    this.getStandardColor = (name) => this.colorArray[ChatCardComponent.hash(name) % this.colorArray.length];
  }

  static hash(e) {
    let hash = 0, i, chr;
    if (e.length === 0) return hash;
    for (i = 0; i < e.length; i++) {
      chr = e.charCodeAt(i);
      hash = ((hash << 3) - hash) + chr;
      hash |= 0; // Convert to 32bit integer
    }
    return hash;
  };

  goToLastChat(timeout = 20) {
    // setTimeout(() => this.lastElementLoc.current.scrollIntoView(false), timeout);
  }

  render() {
    let chatElms = [];
    if (this.props.friend in this.props.allChatMessages) {
      let messages = this.props.allChatMessages[this.props.friend];
      let len = messages.length;
      messages.forEach((message, index) => {
        if (message.sender === this.props.friend) {
          console.log(message.sender, this.props.friend, (message.sender !== this.props.friend))

          chatElms.push(<ChatElement key={index} userIsReceiver content={message.message} timeSent={message.timeSent}/>)

        } else {
          console.log(message.sender, this.props.friend, (message.sender == this.props.friend))

          chatElms.push(<ChatElement key={index} userIsSender content={message.message} timeSent={message.timeSent}/>)
        }
      });
      chatElms = chatElms.slice(len - (this.state.isChatOpened ? len : 3), len);
    }

    this.goToLastChat(20);

    return <React.Fragment>
      <div className={"chat-person-card mdl-card mdl-shadow--2dp mdl-cell " +
      (this.state.isChatOpened ? "mdl-cell--12-col" : "mdl-cell--4-col")}
           style={{backgroundColor: this.getStandardColor(this.props.friend)}}>
        <div className="mdl-card__title">
        <span>
          {this.props.friend}
        </span>
          {this.state.isChatOpened ?
            <React.Fragment>
              <div className="mdl-layout-spacer">
              </div>
              < button className="mdl-button mdl-js-button mdl-button--raised" onClick={this.toggleChatMode}
                       style={{
                         minWidth: "inherit", padding: "0px 7px", height: 'inherit',
                         lineHeight: "28px", background: "lightgrey"
                       }}> X
              </button>
            </React.Fragment>
            :
            ""
          }

        </div>
        <div className="mdl-card__supporting-text  mdl-card--border previous-chat-details "
             style={{width: "initial", height: this.state.isChatOpened ? "50vh" : "inherit"}}>
          <ol className="discussion"
              style={{width: "initial", overflowY: this.state.isChatOpened ? "scroll" : "hidden", height: "100%",}}>
            {chatElms}
            <li ref={this.lastElementLoc}/>
          </ol>
        </div>
        <div className="mdl-card__actions mdl-card--border" style={{padding: "0px"}}>

          {this.state.isChatOpened ?
            <div className="chat-box-message-form" style={{paddingLeft: "20px", width: "100%"}}>
              <div className="mdl-textfield mdl-js-textfield" style={{width: "calc(100% - 100px)"}}>
                <form action="#">
                  <input ref={this.content} className="mdl-textfield__input" type="text" id="asdas"/>
                </form>
              </div>
              <button className="mdl-button mdl-js-button mdl-button--raised mdl-button--accent"
                      onClick={() => {
                        if (this.content.current.value !== "") {
                          this.props.sendMessage(this.props.friend, this.content.current.value);
                        }
                      }}
                      style={{right: "-15px", height: "50px", paddingBottom: "5px"}}>
                <i className="material-icons" style={{color: "white"}}>send</i>
              </button>
            </div>
            :
            <div className="mdl-button mdl-button--colored mdl-js-button mdl-js-ripple-effect"
                 style={{width: "100%", padding: "10px 25px 10px 15px"}} onClick={this.toggleChatMode}>
              <div style={{display: "flex"}}>
                <span>Chat</span>
                <span style={{flex: 1}}/>
                <i className="material-icons" style={{transform: "translate(5px, 7px)"}}>chat</i>
              </div>
            </div>
          }
        </div>
      </div>
    </React.Fragment>
      ;
  }
}

const mapStateToProps = state => ({
    isLoginDialogVisible: state.login.loginDialog.isLoginDialogVisible,
    userData: state.login.userData,
    allChatMessages: state.screen.details.home.chats,
  })
;

const mapDispatchToProps = dispatch => ({
  showLoginDialog: () => dispatch(displayLoginDialog(true)),
  hideLoginDialog: () => dispatch(displayLoginDialog(false)),
  sendMessage: (otherUser, content) => dispatch(sendMessage(otherUser, content)),

});

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ChatCardComponent)

