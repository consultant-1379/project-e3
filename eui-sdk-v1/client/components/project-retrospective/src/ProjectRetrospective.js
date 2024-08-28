/**
 * Component ProjectRetrospective is defined as
 * `<e-project-retrospective>`
 *
 * Imperatively create component
 * @example
 * let component = new ProjectRetrospective();
 *
 * Declaratively create component
 * @example
 * <e-project-retrospective></e-project-retrospective>
 *
 * @extends {LitComponent}
 */
import { definition } from '@eui/component';
import "@eui/table";
import { LitComponent, html } from '@eui/lit-component';
import style from './projectRetrospective.css';

/**
 * @property {Boolean} propOne - show active/inactive state.
 * @property {String} propTwo - shows the "Hello World" string.
 */
@definition('e-project-retrospective', {
  style,
  home: 'project-retrospective',
  props: {
    // viewToggle: { attribute: true, type: String, default: "Team" },
    sprintItems: { attribute: true, type: Array },
    allSprintIds: { attribute: true, default: [] },
    allProjectNames: { attribute: true, default: [] },
    allProjectIds: { attribute: true, default: [] },
    treeData: { attribute: true, type: Array, default: [] },
    tableData: { attribute: true, type: Array, default: [] }
  },
})
export default class ProjectRetrospective extends LitComponent {
  constructor() {
    super();
    (this.sprintStart = undefined),
      (this.sprintEnd = undefined),
      (this.sprintId = ''),
      (this.teamName = ''),
      (this.projectId = 1),
      (this.viewToggle = "Team")
  }

  // Functions to fetch data for team retro page
  getTeam() {
    fetch('http://localhost:8080/team/1')
      .then((response) => response.json())
      .then((data) => {
        console.log('Success. Team:', data);
        this.teamName = data.name;
      })
      .catch((error) => {
        console.error('Error:', error);
      });
  }


  getProjects() {
    let projectNamesForTeam = [];
    let projectIdsForTeam = [];
    fetch('http://localhost:8080/project/team/1')
      .then((response) => response.json())
      .then((data) => {
        console.log('Success. Project:', data);
        data.forEach(elem => {
          projectNamesForTeam.push(elem.name);
          projectIdsForTeam.push(elem.id);
        });

        this.allProjectNames = projectNamesForTeam;
        this.allProjectIds = projectIdsForTeam;
      })
      .catch((error) => {
        console.error('Error:', error);
      });
  }

  completedCardLayout() {
    let layout = 'Nothing to display';
    if (this.sprintItems != null) {
      layout = this.sprintItems.map((item) => {
        if (item.status === "COMPLETED") {
          return html`
          <eui-layout-v0-card card-title=${item.title} subtitle=${item.status}>
          <div slot="content">
            Assigned To: ${item.memberAssignedTo.name}<p>
            Description: ${item.description}<p>
          </div>
          </eui-layout-v0-card>`;
        } else {
          return html``;
        }
      })
    }
    return layout;
  }

  incompleteCardLayout() {
    let layout = 'Nothing to display';
    if (this.sprintItems != null) {
      layout = this.sprintItems.map((item) => {
        if (item.status === "TBD" || item.status === "IN_PROGRESS") {
          return html`
          <eui-layout-v0-card card-title=${item.title} subtitle=${item.status}>
          <div slot="content">
            Assigned To: ${item.memberAssignedTo.name}<p>
            Description: ${item.description}<p>
          </div>
          </eui-layout-v0-card>`;
        } else {
          return html``;
        }
      })
    }
    return layout;
  }

  /**
   * Render the <e-project-retrospective> component. This function is called each time a
   * prop changes.
   */
  render() {
    if (this.viewToggle === "Project") {
      // Getting sprint Ids for the selected Project
      this.getSprints();

      // Setting up tree data to contain the Ids of the sprints
      this.treeData.length = 0;

      for (var i = 0; i < this.allSprintIds.length; i += 2) {
        this.treeData.push(
          {
            "label": "Sprint " + this.allSprintIds[i],
            "data": this.allSprintIds[i],
          },
        )
      }

      // If there is no sprint currently selected, alter tile title & breadcrumb accordingly
      if (this.sprintStart == undefined) {
        this.tileTitle = "Select sprint";
        this.breadcrumbData = {
          path: "lvl-1/lvl-2",
          elements: {
            "lvl-1": { "label": "Team " + this.teamName },
            "lvl-2": { "label": "Project " + this.projectId }
          }
        }
      } else {
        this.tileTitle = "Sprint started: " + this.sprintStart + ", Sprint ended: " + this.sprintEnd;
        this.breadcrumbData = {
          path: "lvl-1/lvl-2/lvl-3",
          elements: {
            "lvl-1": { "label": "Team " + this.teamName },
            "lvl-2": { "label": "Project " + this.projectId },
            "lvl-3": { "label": "Sprint " + this.sprintId }
          }
        }
      }

      let sprintTableColumns = [{ title: `Select sprint`, attribute: "col1" }];
      this.tableData.length = 0;

      // If the sprintItems haven't been fetched yet, just leave the table blank
      if (this.sprintItems != null) {
        sprintTableColumns = [{ title: `Items for sprint ${this.sprintId}`, attribute: "col1" }];
      }

      if (this.allSprintIds == []) {
        this.tileTitle = "No completed sprints for this project";
      }

      return html`
    <eui-base-v0-breadcrumb  @eui-breadcrumb:change="${(event) => this._euiBreadCrumbChange(event)}" .data=${this.breadcrumbData}></eui-base-v0-breadcrumb>

    <div class="body" style="display: flex; flex-wrap: wrap;">
      <eui-base-v0-tree style="flex: 10%; font-size: 20;" @eui-tree:select=${(event) => {
          this._onSelect(event.detail.data);
        }} .data=${this.treeData} navigation></eui-base-v0-tree>
      
      <eui-layout-v0-tile style="flex: 90%;" tile-title="${this.tileTitle}">
        <div slot="content" style="display: flex; flex-wrap: wrap;">
        <div style="flex: 50%;">
        <h3>Incomplete Tasks</h3>
        ${this.incompleteCardLayout()}
        </div>
        <div style="flex: 50%;">
        <h3>Completed Tasks</h3>
        ${this.completedCardLayout()}
        </div>

        </div>
      </eui-layout-v0-tile>
    </div>
    `;

    } else if (this.viewToggle === "Team") {

      // Gets team name
      this.getTeam();

      // Sets breadcrumb to contain team name
      this.breadcrumbData = {
        path: "lvl-1",
        elements: {
          "lvl-1": { "label": "Team " + this.teamName }
        }
      }

      // Gets project names for selected team
      this.getProjects();

      // Loading project names into table
      const projectTableColumns = [{ title: `Project Name`, attribute: "col1" },
      { title: `Project ID`, attribute: "col2" }];
      let data = [];

      let tempNameData = this.allProjectNames.split(",");
      let tempIdData = this.allProjectIds.split(",");
      for (var i = 0; i < tempNameData.length; i++) {
        data.push(
          {
            col1: tempNameData[i],
            col2: tempIdData[i],
          }
        )
      }
      // console.log(this.breadcrumbData['elements']['lvl-1'].label.split(" ")[0]);
      this.tileTitle = "Projects for Team " + this.teamName;

      // Rendering website with above values
      return html`
      <eui-base-v0-breadcrumb @eui-breadcrumb:change="${(event) => this._euiBreadCrumbChange(event)}" .data= ${this.breadcrumbData}></eui-base-v0-breadcrumb>

      <div class="body" style="display: flex; flex-wrap: wrap;">
        <eui-base-v0-tree style="flex: 10%; font-size: 20;" navigation></eui-base-v0-tree>
        <eui-layout-v0-tile style="flex: 90%;" tile-title="${this.tileTitle}">
          <div slot="content">
          <eui-table-v0 striped single-select @eui-table:row-select=${(event) => {
          this.projectId = event.detail[0].col2;
          this._onToggleView("Project");
          this.getSprints();
        }} .columns=${projectTableColumns} .data=${data}></eui-table-v0>
          </div>
      </eui-layout-v0-tile>
      </div>
      `;
    }
  }

  _euiBreadCrumbChange(event) {
    // console.log("euiBreadCrumbChange triggered");
    // if (this.viewToggle === "Sprint") {
    //   console.log("Current view is Sprint, changing to Project");
    //   this._onToggleView("Project");
    // } else if (this.viewToggle === "Project") {
    //   this._onToggleView("Team");
    //   console.log("Current view is Project, changing to Team");
    // }
  }

  _onSelect(sprintID) {
    let items = [];
    fetch('http://localhost:8080/sprint/' + sprintID)
      .then((response) => response.json())
      .then((data) => {
        console.log('Success:', data);
        this.sprintId = data.id;
        this.sprintStart = new Date(data.startDate).toLocaleDateString("en-GB");
        this.sprintEnd = new Date(data.endDate).toLocaleDateString("en-GB");
        this.sprintItems = data.items.forEach(elem => {
          items.push(elem);
        });
        this.sprintItems = items;
      })
      .catch((error) => {
        console.error('Error:', error);
      });
  }

  _onToggleView(view) {
    this.viewToggle = view;
  }

  getSprints() {
    let sprintsFromProject = [];
    fetch('http://localhost:8080/sprint/project/' + this.projectId)
      .then((response) => response.json())
      .then((data) => {
        console.log('Success:', data);
        data.forEach(elem => {
          if (elem.status === "COMPLETED") {
            sprintsFromProject.push(elem.id);
          }
        });

        this.allSprintIds = sprintsFromProject;
      })
      .catch((error) => {
        console.error('Error:', error);
      });
  }
}

/**
 * Register the component as e-project-retrospective.
 * Registration can be done at a later time and with a different name
 */
ProjectRetrospective.register();
