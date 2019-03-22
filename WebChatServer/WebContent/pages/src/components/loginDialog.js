import React from "react";

import {connect} from 'react-redux'
import {displayLoginDialog, loginVerify, signupUser} from "../actions";
import './loginDialog.css';

class LoginDialog extends React.Component {


  constructor(props) {
    super(props);
    this.userNameSignup = "";
    this.passwordSignup = "";
    this.confirmPasswordSignup = "";

    this.userNameLogin = "";
    this.passwordLogin = "";

    this.state = {
      dialogOpen: false,
      statusMessage: ""
    };

    this.loginVerify = (e) => {
      e.preventDefault();
      if (!this.userNameLogin.value.trim() || !this.passwordLogin.value.trim()) {
        this.setState({...this.state, ...{statusMessage: "Please Enter Username/Password"}});
        return
      }
      this.setState({...this.state, ...{statusMessage: ""}});
      this.props.loginVerify(this.userNameLogin.value.trim(), this.passwordLogin.value.trim());
    };

    this.signupUser = (e) => {
      e.preventDefault();
      if (!this.userNameSignup.value.trim() || !this.passwordSignup.value.trim()
        || !this.confirmPasswordSignup.value.trim()) {
        this.setState({...this.state, ...{statusMessage: "Please Enter all fields"}});
        return;
      } else if (this.passwordSignup.value.trim() !== this.confirmPasswordSignup.value.trim()) {
        this.setState({...this.state, ...{statusMessage: "The Passwords don't match"}});
        return;
      }
      this.setState({...this.state, ...{statusMessage: ""}});
      this.props.signupUser(this.userNameSignup.value.trim(), this.passwordSignup.value.trim());
    };

  }

  render() {
    let showMessage = (this.state.statusMessage !== "" || this.props.loginDialogMessage !== "");
    console.log("message:" + showMessage);
    let messageClassName = this.props.isSuccess ? "isa_success" : "isa_error";
    let errorMessage = this.state.statusMessage === "" ? this.props.loginDialogMessage : this.state.statusMessage;
    return (
      <div>
        <dialog className="mdl-dialog" open={this.props.isLoginDialogVisible} style={{zIndex: 15}}>
          <div className="mdl-dialog__content">
            <div className="mdl-tabs mdl-js-tabs mdl-js-ripple-effect">
              <div className="mdl-tabs__tab-bar">
                <a href="#login-panel" className="mdl-tabs__tab is-active">Login</a>
                <a href="#signup-panel" className="mdl-tabs__tab">Signup</a>
              </div>
              <div className="mdl-tabs__panel is-active" id="login-panel">
                {showMessage ? <div className={messageClassName}> {errorMessage}</div> : ""}

                <form action="#">
                  <div className="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input className="mdl-textfield__input" type="text" id="sample1"
                           ref={node => (this.userNameLogin = node)}/>
                    <label className="mdl-textfield__label" htmlFor="sample1">Username</label>
                  </div>
                  <div className="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input className="mdl-textfield__input" type="password" id="sample2"
                           ref={node => (this.passwordLogin = node)}/>
                    <label className="mdl-textfield__label" htmlFor="sample2">Password</label>
                  </div>
                </form>

                <div className="mdl-dialog__actions mdl-dialog__actions--full-width">
                  <button type="button"
                          className="mdl-button  mdl-button--raised mdl-button--colored"
                          style={{textAlign: "center"}} onClick={this.loginVerify}>Login
                  </button>
                </div>

              </div>
              <div className="mdl-tabs__panel" id="signup-panel">
                {showMessage ? <div className={messageClassName}> {errorMessage}</div> : ""}

                <div className="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                  <input className="mdl-textfield__input" type="text" id="sample3"
                         ref={node => (this.userNameSignup = node)}/>
                  <label className="mdl-textfield__label" htmlFor="sample3">Username</label>
                </div>
                <div className="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                  <input className="mdl-textfield__input" type="password" id="sample4"
                         ref={node => (this.passwordSignup = node)}/>
                  <label className="mdl-textfield__label" htmlFor="sample4">Password</label>
                </div>
                <div className="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                  <input className="mdl-textfield__input" type="password" id="sample5"
                         ref={node => (this.confirmPasswordSignup = node)}/>
                  <label className="mdl-textfield__label" htmlFor="sample5">Confirm Password</label>
                </div>
                <div className="mdl-dialog__actions mdl-dialog__actions--full-width">
                  <button type="button"
                          className="mdl-button  mdl-button--raised  mdl-button--colored"
                          style={{textAlign: "center"}} onClick={this.signupUser}>Signup
                  </button>
                </div>
              </div>

            </div>
          </div>

        </dialog>

      </div>

    );
  }
}

const mapStateToProps = state => ({
    isLoginDialogVisible: state.login.loginDialog.isLoginDialogVisible,
    isSuccess: state.login.loginDialog.isSuccess,
    loginDialogMessage: state.login.loginDialog.loginDialogMessage,
  })
;

const mapDispatchToProps = dispatch => ({
  showLoginDialog: () => dispatch(displayLoginDialog(true)),
  hideLoginDialog: () => dispatch(displayLoginDialog(false)),
  loginVerify: (username, password) => dispatch(loginVerify(username, password)),
  signupUser: (username, password) => dispatch(signupUser(username, password)),
});

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(LoginDialog)
