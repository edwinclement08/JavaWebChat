import React, {Component} from "react";

class Header extends Component {
  constructor(props) {
    super(props);
    this.state = {};
  }

  render() {
    return (
      <div className="mdl-layout__header-row">
        <span className="mdl-layout-title">Home</span>
        <div className="mdl-layout-spacer"/>
        <div className="mdl-textfield mdl-js-textfield mdl-textfield--expandable">
          <label className="mdl-button mdl-js-button mdl-button--icon" htmlFor="search">
            <i className="material-icons">search</i>
          </label>
          <div className="mdl-textfield__expandable-holder">
            <input className="mdl-textfield__input" type="text" id="search"/>
            <label className="mdl-textfield__label" htmlFor="search">
              Enter your query...
            </label>
          </div>
        </div>
        <button
          className="mdl-button mdl-js-button mdl-js-ripple-effect mdl-button--icon"
          id="hamburger">
          <i className="material-icons">more_vert</i>
        </button>
        <ul className="mdl-menu mdl-js-menu mdl-js-ripple-effect mdl-menu--bottom-right" htmlFor="hamburger">
          <li className="mdl-menu__item">About</li>
          <li className="mdl-menu__item">Contact</li>
        </ul>
      </div>
    );
  }
}

export default Header;
