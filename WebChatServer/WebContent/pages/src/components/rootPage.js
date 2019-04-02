import React from "react";
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

let PageHolder = connect(state => ({
  visibleScreen: state.screen.visibleScreen
}))(
  (props) => {
    switch (props.visibleScreen) {
      case "home":
        return <HomePage/>;
      case "allUsers":
        return <AllUsersPage/>;
      case "help":
        return <HelpPage/>;
      default:
        return <HomePage/>;
    }
  }
);

let AllUsersPage = connect(state => ({
  users: state.screen.details.home.users
}))((props) => {
  let ChatCardList = [];
  props.users.forEach((val, i) => ChatCardList.push(<ChatCardComponent key={i} friend={val}/>));
  return <div className="mdl-grid ">
    {ChatCardList}
  </div>;
});

let HomePage = connect(state => ({
  friends: state.screen.details.home.friends
}))((props) => {
  let ChatCardList = [];
  props.friends.forEach((val, i) => ChatCardList.push(<ChatCardComponent key={i} friend={val}/>));
  return <div className="mdl-grid ">
    {ChatCardList}
  </div>;
});

let HelpPage = () =>
  <div className="mdl-grid ">
    <p className="mdl-cell--12-col"><h3>Help Pages</h3></p>
    <p className="mdl-cell--12-col">
      <h6>Chat with your friends on the <b><i>Home</i></b> Page </h6>
      <h6>If you can't find your friends on the <b><i>Home</i></b> Page, start chat on the <b><i>All Users</i></b> Page.
      </h6>
    </p>

  </div>;

let NotLoggedInPage = () =>
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
  </div>;


class RootPage extends React.Component {
  render() {
    return (
      <React.Fragment>
        {this.props.userData.loggedIn ? <PageHolder/> : <NotLoggedInPage/>}
      </React.Fragment>
    );
  }
}

export default connect(
  state => ({
    userData: state.login.userData,
  })
)(RootPage)

