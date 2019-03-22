import React from "react";
import {displayLoginDialog} from "../actions";
import {connect} from "react-redux";
import ChatCardComponent from "./ChatCardComponent";

class HomePage extends React.Component {
  render() {
    return (<React.Fragment>
        <ChatCardComponent/>
        <ChatCardComponent/>
        <ChatCardComponent/>
      </React.Fragment>
    );
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
)(HomePage)

