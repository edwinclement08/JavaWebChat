import React from "react";
import {displayLoginDialog, loginVerify, signupUser, signoutUser} from "../actions";
import {connect} from "react-redux";
import "./sideBar.css";

class SideBar extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div>
        <header className="demo-drawer-header">
          <img alt="dd" src=" images/user.jpg" className=" demo-avatar"/>
          {!this.props.userData.loggedIn ?
            <div className=" demo-avatar-dropdown">
              <button type="button"
                      className="mdl-button  mdl-button--raised mdl-button--colored"
                      style={{textAlign: "center", width: "100%"}} onClick={this.props.showLoginDialog}>Login
              </button>
            </div>
            :
            <div className=" demo-avatar-dropdown">
              <span>{this.props.userData.user}</span>
              <div className=" mdl-layout-spacer">
              </div>
              <button
                id=" logoutBtn"
                className=" mdl-button mdl-js-button mdl-js-ripple-effect mdl-button--icon"
                onClick={this.props.signoutUser}>
                <i className="material-icons" role="presentation">
                  exit_to_app
                </i>
              </button>
            </div>
          }
        </header>
        <nav className=" demo-navigation mdl-navigation mdl-color--blue-grey-800" id=" sidebarLinksPlugPoint">
          <a className=" mdl-navigation__link" href="#ds">
            <i className=" mdl-color-text--blue-grey-400 material-icons" role="presentation"> home </i>
            Home
          </a>
          <a className=" mdl-navigation__link" href="#sds">
            <i className=" mdl-color-text--blue-grey-400 material-icons" role="presentation"> inbox </i>
            Inbox </a>
          <div className=" mdl-layout-spacer">
          </div>
          <div className=" mdl-layout-spacer">
          </div>
          <a className=" mdl-navigation__link" href="#sddf">
            <i className=" mdl-color-text--blue-grey-400 material-icons" role="presentation">
              help_outline
            </i>
            <span className=" visuallyhidden">Help</span>
            Help
          </a>
        </nav>
      </div>
    );
  }
}

const mapStateToProps = state => ({
  userData: {
    loggedIn: state.login.userData.loggedIn,
    user: state.login.userData.user,
    token: state.login.userData.token,
  },
});

const mapDispatchToProps = dispatch => ({
  signoutUser: () => dispatch(signoutUser()),
  showLoginDialog: () => dispatch(displayLoginDialog(true)),

});

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SideBar)
