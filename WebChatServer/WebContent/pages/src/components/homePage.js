import React from "react";
import {displayLoginDialog} from "../actions";
import {connect} from "react-redux";
import ChatCardComponent from "./ChatCardComponent";
import noUserLoggedInImg from "./noUserLoggedIn.svg";

import styled from 'styled-components';

const NoUserLoggedInStyle = styled.div`
height:100%;
width: 100%;
display: flex;
justify-content: center;
 opacity: 0.5;
`;

const LimitImgSize = styled.img`
  display: block;
  width: auto;
  height: auto;
  max-width: 100%;
  max-height: 90%;
  margin: 20px auto;
`

class HomePage extends React.Component {
  render() {
    return (
      <React.Fragment>
        {
          this.props.userData.loggedIn ?
            <div className="mdl-grid ">
              <ChatCardComponent receipient="John"/>
              <ChatCardComponent receipient="Suzy"/>
              <ChatCardComponent receipient="Lucifer"/>
              <ChatCardComponent receipient="Edwin"/>
              <ChatCardComponent receipient="Samson"/>
              <ChatCardComponent receipient="Jerin"/>
              <ChatCardComponent receipient="Akash"/>
            </div>
            :
            <div className="mdl-grid " style={{height: "100%"}}>
              <NoUserLoggedInStyle>
                <img alt="no User logged in" src={noUserLoggedInImg} style={{
                  width: "auto",
                  height: "auto",
                  maxWidth: "100%",
                  maxHeight: "90%",
                  margin: "200px",
                }}/>

              </NoUserLoggedInStyle>
            </div>
        }
      </React.Fragment>
    );
  }
}

const mapStateToProps = state => ({
  isLoginDialogVisible: state.login.loginDialog.isLoginDialogVisible,
  userData: state.login.userData,
});

const mapDispatchToProps = dispatch => ({
  showLoginDialog: () => dispatch(displayLoginDialog(true)),
  hideLoginDialog: () => dispatch(displayLoginDialog(false)),
});

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(HomePage)

