/**
 * Component SprintItem is defined as
 * `<e-sprint-item>`
 *
 * Imperatively create component
 * @example
 * let component = new SprintItem();
 *
 * Declaratively create component
 * @example
 * <e-sprint-item></e-sprint-item>
 *
 * @extends {LitComponent}
 */
import { definition } from "@eui/component";
import { LitComponent, html } from "@eui/lit-component";
import style from "./sprintItem.css";


/**
 * @property {Boolean} propOne - show active/inactive state.
 * @property {String} propTwo - shows the "Hello World" string.
 */
@definition("e-sprint-item", {
  style,
  home: "sprint-item",
  props: {
    progressValue: { attribute: true, type: Number },
    dialogToggle: { type: Boolean, default: false },
    addCallBack: { type: Function },
    itemToSubmit: { type: Array },
    // itemToSubmit: { type: Array },
  },
})
export default class SprintItem extends LitComponent {
  constructor() {
    super();
    // initialize

    this.itemCreateLocation = "#item-creation ";
  }

  connectedCallBack() {
    super.connectedCallback();
    this.addCallBack = this.setAttribute("executeRender");
  } //Function allows properties (props) to be passed down to child components
  /**
   * Render the <e-sprint-item> component. This function is called each time a
   * prop changes.
   */
  render() {
    return html`
      <eui-base-v0-button @click=${(event) => this._onToggleDialog(event)}
        >Add Item</eui-base-v0-button
      >
      <eui-base-v0-dialog id="item-creation" label="Item Creation">
        <div
          slot="content"
          style="display: flex; align-items:center; margin: 0 auto;"
        >
          <div class="details" style="text-align:center;">
            Title: &ensp;
            <eui-base-v0-text-field
              id="title"
              maxlength="70"
              required
            ></eui-base-v0-text-field>
            <p>
              Description: &ensp;
              <eui-base-v0-textarea
                id="description"
                maxlength="500"
                required
              ></eui-base-v0-textarea>
            </p>

            <p>
              Assigned to: &ensp;
              <eui-base-v0-dropdown
                id="assigned-to"
                label="Select member..."
                data-type="single"
              >
                <eui-base-v0-menu-item
                  value="1"
                  label="Lucy Kerrigan"
                ></eui-base-v0-menu-item>
                <eui-base-v0-menu-item
                  value="2"
                  label="Jing Sheng Moey"
                ></eui-base-v0-menu-item>
                <eui-base-v0-menu-item
                  value="3"
                  label="Christopher Boland"
                ></eui-base-v0-menu-item>
                <eui-base-v0-menu-item
                  value="4"
                  label="Cameron Scholes"
                ></eui-base-v0-menu-item>
              </eui-base-v0-dropdown>
            </p>

            <p>
              Created by: &ensp;
              <eui-base-v0-dropdown
                id="created-by"
                label="Select member..."
                data-type="single"
              >
                <eui-base-v0-menu-item
                  value="1"
                  label="Lucy Kerrigan"
                ></eui-base-v0-menu-item>
                <eui-base-v0-menu-item
                  value="2"
                  label="Jing Sheng Moey"
                ></eui-base-v0-menu-item>
                <eui-base-v0-menu-item
                  value="3"
                  label="Christopher Boland"
                ></eui-base-v0-menu-item>
                <eui-base-v0-menu-item
                  value="4"
                  label="Cameron Scholes"
                ></eui-base-v0-menu-item>
              </eui-base-v0-dropdown>
            </p>

            <p>
              Category: &ensp;
              <eui-base-v0-dropdown
                id="category"
                label="Select category..."
                data-type="single"
              >
                <eui-base-v0-menu-item
                  value="0"
                  label="Mad"
                ></eui-base-v0-menu-item>
                <eui-base-v0-menu-item
                  value="1"
                  label="Sad"
                ></eui-base-v0-menu-item>
                <eui-base-v0-menu-item
                  value="2"
                  label="Glad"
                ></eui-base-v0-menu-item>
              </eui-base-v0-dropdown>
            </p>

            <p>
              Priority: &ensp;
              <eui-base-v0-dropdown
                id="priority"
                label="Select priority..."
                data-type="single"
              >
                <eui-base-v0-menu-item
                  value="0"
                  label="Low"
                ></eui-base-v0-menu-item>
                <eui-base-v0-menu-item
                  value="1"
                  label="Medium"
                ></eui-base-v0-menu-item>
                <eui-base-v0-menu-item
                  value="2"
                  label="High"
                ></eui-base-v0-menu-item>
              </eui-base-v0-dropdown>
            </p>

            <p>
              Label: &ensp;
              <eui-base-v0-dropdown
                id="label"
                label="Select label..."
                data-type="single"
              >
                <eui-base-v0-menu-item
                  value="0"
                  label="Epic"
                ></eui-base-v0-menu-item>
                <eui-base-v0-menu-item
                  value="1"
                  label="Story"
                ></eui-base-v0-menu-item>
                <eui-base-v0-menu-item
                  value="2"
                  label="Task"
                ></eui-base-v0-menu-item>
                <eui-base-v0-menu-item
                  value="3"
                  label="Subtask"
                ></eui-base-v0-menu-item>
              </eui-base-v0-dropdown>
            </p>

            <p>
              Status: &ensp;
              <eui-base-v0-dropdown
                id="status"
                label="Select status..."
                data-type="single"
              >
                <eui-base-v0-menu-item
                  value="0"
                  selected
                  label="To Be Determined"
                ></eui-base-v0-menu-item>
                <eui-base-v0-menu-item
                  value="1"
                  label="In Progress"
                ></eui-base-v0-menu-item>
                <eui-base-v0-menu-item
                  value="2"
                  label="Complete"
                ></eui-base-v0-menu-item>
              </eui-base-v0-dropdown>
            </p>

            <p></p>
          </div>
        </div>
        <eui-base-v0-button
          icon="save"
          primary
          slot="bottom"
          @click=${(event) => this._onSubmit(event)}
          >Save task</eui-base-v0-button
        >
      </eui-base-v0-dialog>
    `;
  }

  _onToggleDialog(event) {
    this.shadowRoot.querySelector("eui-base-v0-dialog#item-creation").show =
      !this.shadowRoot.querySelector("eui-base-v0-dialog#item-creation").show;
  }

  formReset() {
    //Attempts to clear the create item menu -- currently doesn't completely work
    this.shadowRoot.querySelector(
      this.itemCreateLocation + "eui-base-v0-text-field#title"
    ).value = "";
    this.shadowRoot.querySelector(
      this.itemCreateLocation + "eui-base-v0-textarea#description"
    ).value = "";
    // this.shadowRoot.querySelector('eui-base-v0-dropdown#assigned-to').value[0] = undefined;
    // <eui-base-v0-menu-item value=null label="Unassigned"></eui-base-v0-menu-item>
  }

  async _onSubmit(event) {
    let title = this.shadowRoot.querySelector(
      this.itemCreateLocation + "eui-base-v0-text-field#title"
    ).value;
    let description = this.shadowRoot.querySelector(
      this.itemCreateLocation + "eui-base-v0-textarea#description"
    ).value;
    let assignedTo = this.shadowRoot.querySelector(
      this.itemCreateLocation + "eui-base-v0-dropdown#assigned-to"
    ).value[0];
    let createdBy = this.shadowRoot.querySelector(
      this.itemCreateLocation + "eui-base-v0-dropdown#created-by"
    ).value[0];
    let category = this.shadowRoot.querySelector(
      this.itemCreateLocation + "eui-base-v0-dropdown#category"
    ).value[0];
    let priority = this.shadowRoot.querySelector(
      this.itemCreateLocation + "eui-base-v0-dropdown#priority"
    ).value[0];
    let label = this.shadowRoot.querySelector(
      this.itemCreateLocation + "eui-base-v0-dropdown#label"
    ).value[0];
    let status = this.shadowRoot.querySelector(
      this.itemCreateLocation + "eui-base-v0-dropdown#status"
    ).value[0];

    let item = {};

    // Ensures required fields are completed before submitting
    if (
      title == "" ||
      description == "" ||
      createdBy == undefined ||
      status == undefined
    ) {
      alert(
        "Please fill in the title, description, created by and status fields."
      );
    } else {
      if (assignedTo == undefined) {
        item["assignedTo"] = null;
      } else {
        item["assignedTo"] = parseInt(assignedTo.value);
      }

      if (category == undefined) {
        item["category"] = null;
      } else {
        item["category"] = parseInt(category.value);
      }

      if (priority == undefined) {
        item["priority"] = null;
      } else {
        item["priority"] = priority.label;
      }

      if (label == undefined) {
        item["label"] = null;
      } else {
        item["label"] = parseInt(label.value);
      }

      item = {
        title: title,
        description: description,
        createdBy: parseInt(createdBy.value),
        assignedTo: parseInt(assignedTo.value),
        status: parseInt(status.value),
        comments: [],
      };
      this.itemToSubmit = item;
      this._onToggleDialog(event);
      await this.postData("http://localhost:8080/item", item);
      await this.executeRender();
      await this.addCallBack();
      this.formReset();
    }
  }

   async postData(url, data) {
    data.sprintID = 1;
    await fetch(url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    })
      .then((response) => response.json())
      .then(async (data) => {
        console.log("Success:", data);   
       
      })
      .catch((error) => {
        console.error("Error:", error);
        alert(
          "Create unsuccessful, make sure you are connect to the internet."
        );
      });
  }
}

/**
 * Register the component as e-sprint-item.
 * Registration can be done at a later time and with a different name
 */

SprintItem.register();
