/**
 * App1 is defined as
 * `<e-app-1>`
 *
 * Imperatively create application
 * @example
 * let app = new App1();
 *
 * Declaratively create application
 * @example
 * <e-app-1></e-app-1>
 *
 * @extends {App}
 */
import { definition } from '@eui/component';
import { App, html } from '@eui/app';
import style from './app1.css';
import 'sprint_ui';
import 'sprint-item';
import 'project-retrospective';

@definition("e-app-1", {
  style,
  props: {
    response: { attribute: false },
  },
})
export default class App1 extends App {
  // Uncomment this block to add initialization code
  constructor() {
    super();
    // (this.title = "Hello there!"),
      (this.view = true),
      (this.parentElem = ""),
      (this.sprintID = "");
    // initialize
  }

  conditionalRender(component) {
    if (this.view === false) {
      return "THIS IS NOW FULL POWER";
    } else {
      return component;
    }
  }

  testFunc = (msg) => {
    this.parentElem = msg;
    console.log(this.parentElem);
  };

  changeView() {
    console.log("before" + this.view);
    if ((this.response = !this.response)) {
      this.executeThis;
    }
    this.view == !this;
    console.log("after" + this.view);
  }

  /**
   * Render the <e-app-1> app. This function is called each time a
   * prop changes.
   */
  render() {
    const { EUI } = window;

    const sprintUI = html`<e-sprint_ui
      .expanded=${this.view}
      .parentCallBack=${this.changeView}
    ></e-sprint_ui>`;

    const sprintItem = html`<e-sprint-item></e-sprint-item>`;

    const conditionalRender = this.conditionalRender(sprintUI);

    return html`<div>${sprintUI}</div>`
      ;
  }
}

App1.register();
