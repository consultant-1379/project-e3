const externals = {
  apps: [{
    path: "app-1",
    entry: "App1"
  }],
  components: {
    default: [],
    shareable: [{
      path: "sprint_ui",
      entry: "Sprint_ui"
    }, {
      path: "sprint-item",
      entry: "SprintItem"
    }, {
      path: "project-retrospective",
      entry: "ProjectRetrospective"
    }]
  },
  panels: [],
  plugins: []
};
module.exports = externals;