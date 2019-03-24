import React, {Component} from "react";
import {connect} from 'react-redux'
import {displayLoginDialog} from "../actions/login";

class PageMask extends Component {
  render() {
    return (<div style={{
      position: "fixed", left: 0, right: 0, bottom: 0,
      top: 0, backgroundColor: "rgba(0, 0, 0, 0.25)", zIndex: 10, display: this.props.display ? "" : "none",
      transition: "backgroundColor 0.3s"
    }} onClick={this.props.hideLoginDialog}/>);
  }
}

let mapStateToProps = state => ({
    display: state.login.loginDialog.isLoginDialogVisible
  })
;

let mapDispatchToProps = dispatch => ({
  hideLoginDialog: () => dispatch(displayLoginDialog(false)),
});


export default connect(mapStateToProps, mapDispatchToProps)(PageMask);