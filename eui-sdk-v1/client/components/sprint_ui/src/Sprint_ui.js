/**
 * Component Sprint_ui is defined as
 * `<e-sprint_ui>`
 *
 * Imperatively create component
 * @example
 * let component = new Sprint_ui();
 *
 * Declaratively create component
 * @example
 * <e-sprint_ui></e-sprint_ui>
 *
 * @extends {LitComponent}
 */
import { definition } from "@eui/component";
import "@eui/table";
import { LitComponent, html } from "@eui/lit-component";
import style from "./sprint_ui.css";

/**
 * @property {Boolean} propOne - show active/inactive state.
 * @property {String} propTwo - shows the "Hello World" string.
 */
@definition("e-sprint_ui", {
  style,
  home: "sprint_ui",
  props: {
    propOne: { attribute: true, type: Boolean, default: false },
    itemData: { attribute: true, type: Array, default: [] },
    parentCallBack: { type: Function },
  },
})
export default class Sprint_ui extends LitComponent {
  /**
   * Render the <e-sprint_ui> component. This function is called each time a
   * prop changes.
   */
  constructor() {
    super();
    (this.flag = false),
      (this.dataArray = null),
      this.sprintID,
      (this.completedHeight = ""),
      (this.TBDHeight = ""),
      (this.inProgHeight = "");
    // initialize
  }

  async getItemData() {
    await fetch("http://localhost:8080/item/sprint/1")
      .then((response) => {
        //handle response
        console.log("calling...");
        return response.json();
      })
      .then((data) => {
        //handle data
        console.log(data);
        console.log("loading...");
        this.dataArray = data;
        this.sprintID = this.dataArray[0].sprintID;
      })
      .catch((error) => {
        console.log("this is an error: " + error);
      });
  }

  async updateData(url, data) {
    data.sprintID = 1;
    await fetch(url, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    })
      .then(async () => {
        await this.getItemData();
        await this.executeRender();
      })
      .catch((error) => {
        console.error("Error:", error);
        alert(
          "Create unsuccessful, make sure you are connect to the internet."
        );
      });
  }

  async postSprint(url, data) {
    await fetch(url, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    })
      .then(async () => {
        // await this.getItemData();
        // await this.executeRender();
      })
      .catch((error) => {
        console.error("Error:", error);
        alert(
          "Create unsuccessful, make sure you are connect to the internet."
        );
      });
  }

  triggerComponent = () => {
    this.flag = !this.flag;
    this.postSprint("http://localhost:8080/sprint", {id: this.sprintID, status: 2});
    this.executeRender();
  };

  connectedCallBack() {
    super.connectedCallback();
    this.props.expanded = this.setAttribute("view");
    this.parentCallBack = this.setAttribute("changeView");
  } //Function allows properties (props) to be passed down to child components

  didConnect = async () => {
    await this.getItemData();
    await this.executeRender();
    await this.parentCallBack();
  }; //Function executes as soon as the node (Sprint_ui component) is mounted on the DOM

  executeThis = async () => {
    await this.getItemData();
    await this.executeRender();
  };

  conditionalRender(component) {
    if (this.props.itemData === 0) {
      return "Component has not rendered";
    } else {
      return component;
    }
  }

  statusTBD() {
    const r = [];
    var height = 0;
    if (this.dataArray !== null) {
      this.dataArray.map(({ id, sprintID, status, title, description }) => {
        const inProgStatusUpdate = {
          id: id,
          status: 1,
        };
        const completedStatusUpdate = {
          id: id,
          status: 2,
        };
        if (status == "TBD") {
          height += 234;
          r.push(html`<div style="border-bottom: 10px; width: 500px;
            border-style: solid;
            border-radius: 2px;
            border-style:solid;
            border-width: 2px;
            margin-top:20px;
            padding: 20px;
            border-color: red">
            
            <h3 style="color:black; font-size: 23px;"><hr><b>Task ID: ${id}</b>
          <hr></h3>

                <div style="background-color: #FFCCCB;;
                margin-top: 10px;
                border-style: solid;
                border-width: 0.5px;
                border-radius: 2px;">
                <p style="padding:2px;"><b>Sprint id: </b> ${sprintID}</p></u>
                <p style="padding:2px;"><b>Title: </b>${title}</p></u>
                <p style="padding:2px;"><b>Description: </b>${description}</p></u>
                </div>
                <p style="background-color: #FFCCCB;
                padding:2px;
                border-style: solid;
                border-width: 0.5px;
                border-radius: 2px;
                border-radius: 2px;
                "><b>Status: </b> <u>${status}</p></u>
                <eui-base-v0-button warning
                @click=${() => {
                  this.updateData(
                    "http://localhost:8080/item",
                    inProgStatusUpdate
                  );
                }}
                >In Progress</eui-base-v0-button
              >
              <eui-base-v0-button
              warning
                @click=${() => {
                  this.updateData(
                    "http://localhost:8080/item",
                    completedStatusUpdate
                  );
                }}
                >Completed</eui-base-v0-button
              > 
              </div>
              </div>
              <br>
              <br>`);
        }
      });
    }
    this.TBDHeight = height.toString() + "px";
    return r.map((r) => r);
  }

  statusPROG() {
    const r = [];
    var height = 0;
    if (this.dataArray !== null) {
      this.dataArray.map(({ id, sprintID, status, title, description }) => {
        if (status == "IN_PROGRESS") {
          height += 234;
          const TBDStatusUpdate = {
            id: id,
            status: 0,
          };
          const completedStatusUpdate = {
            id: id,
            status: 2,
          };
          console.log(status);
          r.push(html`<div style="border-bottom: 10px; width: 500px;
            border-style: solid;
            border-radius: 5px;
            border-width: 2px;
            margin-top:20px;
            padding: 5px;
            border-color: #0275d8;">
            
            <h3 style="color:black; font-size: 23px;"><b>Task ID: ${id}</b></h3>
                <p style="padding:2px;"><b>Sprint id: </b> ${sprintID}</p></u>
                <p style="padding:2px;"><b>Title: </b>${title}</p></u>
                <p style="padding:2px;"><b>Description: </b>${description}</p></u>
                
                <p style="background-color: lightgreen;
                padding:2px;
                border-radius: 2px;
                "><b>Status: </b> <u>${status}</p></u>
              <eui-base-v0-button
              primary
                @click=${() => {
                  this.updateData(
                    "http://localhost:8080/item",
                    completedStatusUpdate
                  );
                }}
                >Completed</eui-base-v0-button
              >
              <eui-base-v0-button
              primary
                @click=${() => {
                  this.updateData(
                    "http://localhost:8080/item",
                    TBDStatusUpdate
                  );
                }}
                >To Do</eui-base-v0-button
              >
              </div>
              <br>
              <br>
               `);
        }
      });
    }
    this.inProgHeight = height.toString() + "px";
    return r.map((r) => r);
  }

  statusCOMPLETED() {
    const r = [];
    const statusCompletedArray = [];
    var height = 0;
    if (this.dataArray !== null) {
      this.dataArray.map(({ id, sprintID, status, title, description }) => {
        if (status == "COMPLETED") {
          height += 234;
          const TBDStatusUpdate = {
            id: id,
            status: 0,
          };
          const inProgStatusUpdate = {
            id: id,
            status: 1,
          };
          statusCompletedArray.push({ id: id, status: status });
          r.push(html`<div style="border-bottom: 10px; width: 500px;
            border-style: solid;
            border-radius: 5px;
            border-width: 2px;
            margin-top:20px;
            padding: 5px;
            border-color: #0275d8;"><div style="background-color: lightgrey; padding:4px;
            border-style: solid;
            border-radius:5px;
            border-width:0.1px;
            opacity: 0.7;
            " 
            ><h3 style="color:black; font-size: 23px;"><b>Task ID: ${id}</b></h3></div>
                <div style="background-color: lightblue;
                border-radius: 2px;
                "><p style="padding:2px;"><b>Sprint id: </b> ${sprintID}</p></u>
                <p style="padding:2px;"><b>Title: </b>${title}</p></u>
                <p style="padding:2px;"><b>Description: </b>${description}</p></u>
                </div>
                <p style="background-color: lightblue;
                padding:2px;
                border-radius: 2px;
                "><b>Status: </b> <u>${status}</p></u>
              <eui-base-v0-button
              primary
                @click=${() => {
                  this.updateData(
                    "http://localhost:8080/item",
                    inProgStatusUpdate
                  );
                }}
                >In Progress</eui-base-v0-button
              >
              <eui-base-v0-button
              primary
                @click=${() => {
                  this.updateData(
                    "http://localhost:8080/item",
                    TBDStatusUpdate
                  );
                }}
                >To Do</eui-base-v0-button
              > </div>
              <br>
              <br>`);
        }
      });
    }
    this.completedHeight = height.toString() + "px";
    this.completedObjectsArray = statusCompletedArray;
    return r.map((r) => r);
  }

  _onCreateReminder(e) {
    this.shadowRoot.querySelector("eui-base-v0-dialog#email-creation").show =
      !this.shadowRoot.querySelector("eui-base-v0-dialog#email-creation").show;
  }

  _submitOpenEmail(e) {
    var baseLocation = "#email-creation";
    var inputLocation = " eui-base-v0-text-field";
    var datepickerLocation = " eui-base-v0-datepicker";

    let title = this.shadowRoot.querySelector(
      baseLocation + inputLocation + "#title"
    ).value;
    let date = this.shadowRoot.querySelector(
      baseLocation + datepickerLocation + "#date"
    ).date;
    let time = this.shadowRoot.querySelector(
      baseLocation + inputLocation + "#time"
    ).value;

    var emailSub = "Meeting for Sprint(" + this.today + ")";
    var emailBody =
      "Hi all, this is a meeting reminder for " +
      title +
      " on " +
      date +
      " at " +
      time +
      ".";
    let emailTo = "";

    if (title.trim() && date.toString().trim() && time.toString().trim()) {
      window.open(
        "mailto:" + emailTo + "?subject=" + emailSub + "&body=" + emailBody
      );
    } else {
      alert("Please fill in all fields");
    }
  }

  render() {
    this.getItemData();

    var today = new Date();
    var dd = String(today.getDate()).padStart(2, "0");
    var mm = String(today.getMonth() + 1).padStart(2, "0");
    var yyyy = today.getFullYear();
    today = this.today = mm + "/" + dd + "/" + yyyy;

    this.emailTitle = "Meeting for Sprint(" + this.today + ")";

    const sprintItem = html`<e-sprint-item
      .addCallBack=${this.executeThis}
    ></e-sprint-item>`;

    const projectRetro = html`<e-project-retrospective
      .addCallBack=${this.executeThis}
      ></e-project-retrosepctive>`;

    const columnsTBD = [{ title: `Tasks to start`, attribute: "id" }];

    const dataTBD = [{ id: "Click for list of items", colorBand: "red" }];

    const columnsPROG = [{ title: `Tasks in progress`, attribute: "id" }];

    const dataPROG = [{ id: "Click for list of items", colorBand: "green" }];

    const columnsCOMPLETED = [{ title: `Tasks completed`, attribute: "id" }];
    const dataCOMPLETED = [
      { id: "Click for list of items", colorBand: "blue" },
    ];

    const statusPROGHtml = this.statusPROG();
    const statusTBDHtml = this.statusTBD();
    const statusCOMPLETEDHtml = this.statusCOMPLETED();

    if (this.dataArray == null) {
      const startItem = html`<h1>Please add an item to begin a sprint:</h1>
        <hr />
        <e-sprint-item .addCallBack=${this.executeThis}></e-sprint-item>`;
      return html` <div>${startItem}</div> `;
    }
    if (this.flag == false && this.dataArray != null) {
      return html`<div>
        <div id="header">
          <h1>Sprint to start: ${this.today}</h1>
          <hr />
          <div>
            <eui-base-v0-button
              @click=${() => {
                this.triggerComponent();
              }}
              >End Sprint</eui-base-v0-button
            >
            <eui-base-v0-button
              primary
              @click=${(event) => this._onCreateReminder(event)}
              >Create Email Reminder</eui-base-v0-button
            >
          </div>
        </div>

        <eui-table-v0
          .columns=${columnsTBD}
          .data=${dataTBD}
          expandable
          expanded-row-height=${this.TBDHeight}
          .custom=${{
            onCreatedDetailsRow: (row) => html`${statusTBDHtml}`,
          }}
        >
        </eui-table-v0>
        <br />
        <br />
        <eui-table-v0
          .columns=${columnsPROG}
          .data=${dataPROG}
          expandable
          expanded-row-height=${this.inProgHeight}
          .custom=${{
            onCreatedDetailsRow: (row) => html`${statusPROGHtml}`,
          }}
        >
        </eui-table-v0>
        <br />
        <br />
        <eui-table-v0
          .columns=${columnsCOMPLETED}
          .data=${dataCOMPLETED}
          expandable
          expanded-row-height=${this.completedHeight}
          .custom=${{
            onCreatedDetailsRow: (row) => html`${statusCOMPLETEDHtml}`,
          }}
        ></eui-table-v0>

        <div>${sprintItem}</div>

        <eui-base-v0-dialog id="email-creation" label="Email Reminder Creation">
          <div
            slot="content"
            style="display: flex; align-items:center; margin: 0 auto;"
          >
            <div class="details" style="text-align:center;">
              <div class="dialog_details">
                <div class="dialog_detail_row">
                  <p>Title:</p>
                  <p />
                  <eui-base-v0-text-field
                    placeholder="Enter the email title here"
                    value=${this.emailTitle}
                    id="title"
                  ></eui-base-v0-text-field>
                </div>

                <div class="dialog_detail_row">
                  <p>Date:</p>
                  <p />
                  <eui-base-v0-datepicker
                    id="date"
                    date=${this.today}
                  ></eui-base-v0-datepicker>
                </div>

                <div class="dialog_detail_row">
                  <p>Time:</p>
                  <p />
                  <eui-base-v0-text-field
                    placeholder="eg:4.05pm"
                    id="time"
                  ></eui-base-v0-text-field>
                </div>
              </div>
            </div>
          </div>
          <eui-base-v0-button
            icon="send"
            primary
            slot="bottom"
            @click=${this._submitOpenEmail.bind(this)}
            >create email</eui-base-v0-button
          >
        </eui-base-v0-dialog>
      </div>`;
    }
    if (this.flag == true) {
      const projectRetro = html`<e-project-retrospective
      ></e-project-retrosepctive>`;
      return html`<div>${projectRetro}</div>`;
    }
  }
}

/**
 * Register the component as e-sprint_ui.
 * Registration can be done at a later time and with a different name
 */
Sprint_ui.register();
