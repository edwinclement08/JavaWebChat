import React from "react";
import {displayLoginDialog} from "../actions";
import {connect} from "react-redux";

class ChatCardComponent extends React.Component {
  render() {
    return (
      <React.Fragment>
        <div className="chat-person-card mdl-card mdl-shadow--2dp">
          <div className="mdl-card__title">
            <span>
              John
            </span>
          </div>
          <div className="mdl-card__supporting-text  mdl-card--border previous-chat-details ">
            <h6>
              You: Heya bro...?
            </h6>
          </div>

          <div className="mdl-card__actions mdl-card--border" style={{padding: "0px"}}>
            <div className="mdl-button mdl-button--colored mdl-js-button mdl-js-ripple-effect"
                 style={{width: "100%", padding: "10px 25px 10px 15px"}}>
              <div style={{display: "flex"}}>
                <span>Chat</span>
                <span style={{flex: 1}}/>
                <i className="material-icons" style={{transform: "translate(5px, 7px)"}}>chat</i>
              </div>
            </div>
          </div>
        </div>

      </React.Fragment>);
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

