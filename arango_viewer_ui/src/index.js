import "./styles.css";
import cytoscape from "cytoscape";
import dagre from "cytoscape-dagre";
import axios from "axios";

cytoscape.use(dagre);

const startTxInput = document.createElement("input");
startTxInput.oninput = (ev) => {
    buildTxTree(ev.target.value)
};
const startTxLabel = document.createElement("label");
startTxLabel.innerHTML = "start transaction";
const startTxDiv = document.createElement("div");
startTxDiv.id = "startTxDiv"
startTxLabel.appendChild(document.createElement("br"))
startTxLabel.appendChild(startTxInput)
startTxDiv.appendChild(startTxLabel)
document.body.appendChild(startTxDiv);

const buildTxTree = (startTx) => {
    axios.get("http://localhost:8080/data/" + startTx)
        .then(({data}) => {
            const elements = data.map(element => {
                return {
                    data: element,
                    classes: ["node-description", element.type]
                }
            });
            const cy = cytoscape({
                container: document.getElementById("cy"),
                elements: elements,
                style: [
                    {
                        selector: "core",
                        css: {
                            "active-bg-size": 0
                        }
                    },
                    {
                        selector: "node",
                        css: {
                            width: "50px",
                            height: "50px",
                            label: 'data(id)',
                        },

                    },
                    {
                        selector: "edge",
                        css: {
                            width: 1,
                            "line-color": "#b8b8b8",
                            "curve-style": "segments",
                            "segment-weights": 0,
                            "segment-distances": 0,
                            "edge-distances": "node-position",
                            "source-endpoint": "90deg",
                            "target-endpoint": "270deg",
                            "target-arrow-shape": "triangle",
                            "control-point-step-size": 150
                        }
                    },
                    {
                        selector: ".hidden",
                        css: {
                            "display": "none"
                        }
                    }
                ],

                layout: {
                    name: "dagre",
                    rankDir: "LR",
                    fit: true,
                    padding: 50
                },
                zoomingEnabled: true,
                userZoomingEnabled: true,
                //autoungrabify: true,
                wheelSensitivity: 0.5
            });

            const hideSuccessors = (node) => {
                node.toggleClass("collapsed")
                if (node.hasClass("collapsed")) {
                    node.successors().addClass("hidden");
                } else {
                    node.successors().removeClass("hidden");
                }
            }

            const infoPanel = document.getElementById("infoPanel")
            cy.on('click', 'node', function (e) {
                infoPanel.innerText = JSON.stringify(e.target.data().description, null, 2);
            });
            cy.on('click', 'edge', function (e) {
                infoPanel.innerText = JSON.stringify(e.target.data().description, null, 2);
            });
            cy.on('dblclick', 'node', function (e) {
                hideSuccessors(e.target);
            });
            const nodeHtmlLabel = require("cytoscape-node-html-label");
            if (typeof cytoscape("core", "nodeHtmlLabel") === "undefined") {
                nodeHtmlLabel(cytoscape);
            }
            cy.nodeHtmlLabel([
                {
                    query: ".node-description.collapsed",
                    tpl: function () {
                        return `<span class="nodeCollapsedSpan">+</span>`;
                    }
                }
            ]);
            const changeNodeTypeColor = (typeClass, color) => {
                cy.$(typeClass)
                    .style({
                        backgroundColor: color,
                        lineColor: color,
                        targetArrowColor: color
                    })
            }
            const controlPanel = document.getElementById("controlPanel")
            controlPanel.innerHTML = ""
            new Set(data.map(({type}) => type))
                .forEach(type => {
                    const randomTypeColor = "#" + Math.floor(Math.random() * 16777215).toString(16);
                    changeNodeTypeColor("." + type, randomTypeColor)
                    const changeTypeColorInput = document.createElement("input");
                    changeTypeColorInput.id = type + "changeColorInput";
                    changeTypeColorInput.type = "color";
                    changeTypeColorInput.value = randomTypeColor;
                    changeTypeColorInput.oninput = (ev) => {
                        changeNodeTypeColor("." + type, ev.target.value)
                    };
                    const changeTypeColorLabel = document.createElement("label");
                    changeTypeColorLabel.innerHTML = type;
                    const changeTypeColorDiv = document.createElement("div");
                    changeTypeColorLabel.appendChild(document.createElement("br"))
                    changeTypeColorLabel.appendChild(changeTypeColorInput)
                    changeTypeColorDiv.appendChild(changeTypeColorLabel)
                    controlPanel.appendChild(changeTypeColorDiv);
                })
        })
}