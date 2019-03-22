import React, {Component} from "react";

class SideBarLinks extends Component {

  render() {
    return (
      <React.Fragment>
        <a className="mdl-navigation__link" href="">
          <i className="mdl-color-text--blue-grey-400 material-icons" role="presentation"> home </i>
          Home
        </a>
        <a className="mdl-navigation__link" href="">
          <i className="mdl-color-text--blue-grey-400 material-icons" role="presentation"> inbox </i>
          Inbox </a>
        <div className="mdl-layout-spacer">
        </div>
        <div className="mdl-layout-spacer">
        </div>
        <a className="mdl-navigation__link" href="">
          <i className="mdl-color-text--blue-grey-400 material-icons" role="presentation">
            help_outline
          </i>
          <span className="visuallyhidden">Help</span>
          Help
        </a>
      </React.Fragment>

    );
  }
}

export default SideBarLinks;
