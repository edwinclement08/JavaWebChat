import React from "react";
import {displayLoginDialog} from "../actions/login";
import {connect} from "react-redux";
import styled from 'styled-components';

import "./ChatCardComponent.css"

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
          <span>{this.props.userIsReceiver ? this.props.sender + " • " : ""}51 min</span>
        </div>
      </li>
    );
  }
}

class ChatCardComponent extends React.Component {
  constructor(props) {
    super(props);
    this.colorArray = ["#4b5dbd", "#44a1f4", "#4ab2f6", "#4dc3da", "#3a9f93", "#5eb659", "#826051", "#6d8896",
      "#a8a8a7", "#f36624", "#ef543b", "#e6396c", "#a337b9", "#7247c0"];

    this.state = {
      isChatOpened: false
    };

    this.hash = (e) => {
      let hash = 0, i, chr;
      if (e.length === 0) return hash;
      for (i = 0; i < e.length; i++) {
        chr = e.charCodeAt(i);
        hash = ((hash << 3) - hash) + chr;
        hash |= 0; // Convert to 32bit integer
      }
      return hash;
    };
    this.toggleChatMode = () => {
      this.setState(Object.assign({}, this.state, {isChatOpened: !this.state.isChatOpened}))
    };
    this.getStandardColor = (name) => this.colorArray[this.hash(name) % this.colorArray.length];

  }

  render() {
    let totalChatElems = [
      <ChatElement userIsSender content={"Nice to Meet You"}/>,
      <ChatElement userIsReceiver sender="Timothy" content={"Whats up2"}/>,
      <ChatElement userIsSender content={"Whats up"}/>,
      <ChatElement userIsReceiver sender="Timothy" content={"Whats up4"}/>,
      <ChatElement userIsSender content={"Whats up"}/>,
      <ChatElement userIsReceiver sender="Timothy" content={"Whats up6"}/>,
      <ChatElement userIsSender content={"Whats up"}/>,
      <ChatElement userIsReceiver sender="Timothy" content={"Whats up8"}/>,
      <ChatElement userIsSender content={"Whats up9"}/>,
    ];
    let len = totalChatElems.length;
    let chatElems = totalChatElems.slice(len - (this.state.isChatOpened ? len : 3), len);


    return <React.Fragment>
      <div className={"chat-person-card mdl-card mdl-shadow--2dp mdl-cell " +
      (this.state.isChatOpened ? "mdl-cell--12-col" : "mdl-cell--4-col")}
           style={{backgroundColor: this.getStandardColor(this.props.recipient)}}>
        <div className="mdl-card__title">
        <span>
          {this.props.recipient}
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
            {chatElems}
          </ol>
        </div>
        <div className="mdl-card__actions mdl-card--border" style={{padding: "0px"}}>

          {this.state.isChatOpened ?
            <div className="chat-box-message-form" style={{paddingLeft: "20px", width: "100%"}}>
              <div className="mdl-textfield mdl-js-textfield" style={{width: "calc(100% - 100px)"}}>
                <form action="#">
                  <input className="mdl-textfield__input" type="text" id="asdas"/>
                </form>
              </div>
              <button className="mdl-button mdl-js-button mdl-button--raised mdl-button--accent"
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
  })
;

const mapDispatchToProps = dispatch => ({
  showLoginDialog: () => dispatch(displayLoginDialog(true)),
  hideLoginDialog: () => dispatch(displayLoginDialog(false)),
});

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ChatCardComponent)

