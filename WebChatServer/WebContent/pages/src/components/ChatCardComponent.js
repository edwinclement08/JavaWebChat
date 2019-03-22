import React from "react";
import {displayLoginDialog} from "../actions";
import {connect} from "react-redux";
import styled from 'styled-components';

import "./ChatCardComponent.css"


class ChatElement extends React.Component {
  constructor(props) {
    super(props);
  }

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
      this.setState(Object.assign({}, this.state, {isChatOpened: true}))
    };
    this.getStandardColor = (name) => this.colorArray[this.hash(name) % this.colorArray.length];
    console.log("wddwwd " + this.getStandardColor(this.props.receipient))
  }

  render() {
    let chatElems = [
      <ChatElement userIsSender content={"Nice to Meet You"}/>,
      <ChatElement userIsReceiver sender="Timothy" content={"Whats up"}/>,
    ];

    return <React.Fragment>
      <div className={"chat-person-card mdl-card mdl-shadow--2dp mdl-cell " +
      (this.state.isChatOpened ? "mdl-cell--12-col" : "mdl-cell--4-col")}
           style={{backgroundColor: this.getStandardColor(this.props.receipient)}}>
        <div className="mdl-card__title">
        <span>
          {this.props.receipient}
        </span>
          <div className="mdl-layout-spacer">
          </div>
          <button className="mdl-button mdl-js-button mdl-button--raised" onClick={() => alert("d")}
                  style={{
                    minWidth: "inherit", padding: "0px 7px", height: 'inherit',
                    lineHeight: "28px", background: "lightgrey"
                  }}> X
          </button>
        </div>
        <div className="mdl-card__supporting-text  mdl-card--border previous-chat-details ">
          <ol className="discussion">
            {chatElems}
          </ol>
        </div>
        <div className="mdl-card__actions mdl-card--border" style={{padding: "0px"}}>
          <div className="mdl-button mdl-button--colored mdl-js-button mdl-js-ripple-effect"
               style={{width: "100%", padding: "10px 25px 10px 15px"}} onClick={this.toggleChatMode}>
            <div style={{display: "flex"}}>
              <span>Chat</span>
              <span style={{flex: 1}}/>
              <i className="material-icons" style={{transform: "translate(5px, 7px)"}}>chat</i>
            </div>
          </div>
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

