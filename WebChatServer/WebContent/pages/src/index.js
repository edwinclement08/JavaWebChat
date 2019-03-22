import React from "react";
import {render} from 'react-dom'
import ReactDOM from "react-dom";
import {Provider} from 'react-redux';
import thunkMiddleware from 'redux-thunk'
import {createLogger} from 'redux-logger'
import {createStore, applyMiddleware} from "redux";

import {composeWithDevTools} from 'redux-devtools-extension';

import "./index.css";
import * as serviceWorker from "./serviceWorker";

import Header from "./components/header";
import SideBar from "./components/sideBar";
import PageMask from "./components/pageMask";
import HomePage from "./components/homePage";
import LoginDialog from "./components/loginDialog"

import reducer from './reducers';
import {displayLoginDialog,} from './actions';

const HomePagePortal = name => ReactDOM.createPortal(<HomePage/>, document.getElementById("homePagePlugPoint"));
const SideBarPortal = props => ReactDOM.createPortal(<SideBar/>, document.getElementById("sidebarPlugPoint"));
const HeaderPortal = props => ReactDOM.createPortal(<Header/>, document.getElementById("headerPlugPoint"));
const LoginPortal = (props) => ReactDOM.createPortal(<LoginDialog/>, document.getElementById("loginDialog"));


const loggerMiddleware = createLogger();
const store = createStore(
  reducer, /* preloadedState, */
  composeWithDevTools(
    applyMiddleware(
      thunkMiddleware, // lets us dispatch() functions
      loggerMiddleware // neat middleware that logs actions
    )
    // other store enhancers if any
  )
);

console.log(store.getState());
// const unsubscribe = store.subscribe(() => console.log(store.getState()));

store.dispatch(displayLoginDialog(true));


render(
  <Provider store={store}>
    <PageMask display={true}/>
    <LoginPortal/>
    <HomePagePortal/>
    <SideBarPortal/>
    <HeaderPortal/>
  </Provider>
  , document.querySelector("#mainroot")
);

serviceWorker.unregister();
