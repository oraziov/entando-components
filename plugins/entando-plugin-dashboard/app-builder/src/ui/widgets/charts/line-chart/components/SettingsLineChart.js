import React, {Component} from "react";
import {Field, FieldArray} from "redux-form";
import {
  Row,
  Col,
  FormGroup,
  ControlLabel,
  FieldLevelHelp
} from "patternfly-react";

import {formattedText, required, minLength, maxLength} from "@entando/utils";
import FormattedMessage from "ui/i18n/FormattedMessage";
import SwitchRenderer from "ui/common/form/SwitchRenderer";

import FieldArrayDropDownMultiple from "ui/widgets/charts/common/components/FieldArrayDropDownMultiple";

import {inputTextField} from "ui/widgets/charts/helper/renderFields";

const maxLength30 = maxLength(30);
const minLength3 = minLength(3);

const wrapInputTextField = (name, label, append, disabled = false) => {
  return (
    <Field
      name={name}
      component={inputTextField}
      label={label}
      react-bootstrap-typeahead
      validate={[required, minLength3, maxLength30]}
      disabled={disabled}
      append={formattedText(append)}
    />
  );
};

class SettingsLineChart extends Component {
  constructor(props) {
    super(props);
    this.toogleSelected = this.toogleSelected.bind(this);
  }

  toogleSelected(id, key) {
    let temp = this.state.columnsY.find(f => f.key === key);
    temp.selected = !temp.selected;
    this.setState(prevState => ({
      columnsY: [...prevState.columnsY, ...temp]
    }));
  }

  chooseTimeFormat() {
    return (
      <div className="SettingsLineChart__timeformat-container">
        <div className="radio">
          <label>
            <Field
              name="axis.x.tick.format"
              component="input"
              type="radio"
              value="%Y-%m-%d"
            />
            <FormattedMessage id="plugin.chart.timeFormat-Y-M-D" />
          </label>
        </div>
        <div className="radio">
          <label>
            <Field
              name="axis.x.tick.format"
              component="input"
              type="radio"
              value="%d-%m-%Y"
            />
            <FormattedMessage id="plugin.chart.timeFormat-D-M-Y" />
          </label>
        </div>
        <div className="radio">
          <label>
            <Field
              name="axis.x.tick.format"
              component="input"
              type="radio"
              value="%m-%d-%Y"
            />
            <FormattedMessage id="plugin.chart.timeFormat-M-D-Y" />
          </label>
        </div>
      </div>
    );
  }

  rederAxisX() {
    return (
      <Col xs={4} className="SettingsLineChart__col">
        <FormGroup className="SettingsLineChart__form-group">
          <ControlLabel>
            <FormattedMessage id="plugin.chart.Xaxis" />
          </ControlLabel>
        </FormGroup>
        {wrapInputTextField(
          "axis.x.label",
          "plugin.chart.labelXaxis",
          "plugin.table.requirement"
        )}
        <FormGroup>
          <Field name="axis.x.type" component="select" className="form-control">
            <option value="indexed">Default</option>
            <option value="timeseries">Time</option>
          </Field>
        </FormGroup>
        {this.props.axisXType === "timeseries" ? this.chooseTimeFormat() : null}
      </Col>
    );
  }

  rederAxisY() {
    const {optionColumns, optionColumnYSelected} = this.props;

    return (
      <Col xs={4} className="SettingsLineChart__col">
        <FormGroup className="SettingsLineChart__form-group">
          <ControlLabel>
            <FormattedMessage id="plugin.chart.Yaxis" />
          </ControlLabel>
        </FormGroup>
        {wrapInputTextField(
          "axis.y.label",
          "plugin.chart.labelYaxis",
          "plugin.table.requirement"
        )}
        <FieldArray
          className="SettingsLineChart__column-selected"
          name="columns.y"
          component={FieldArrayDropDownMultiple}
          optionColumns={optionColumns}
          optionColumnYSelected={optionColumnYSelected}
        />
      </Col>
    );
  }

  rederAxisY2() {
    const {optionColumns, optionColumnY2Selected} = this.props;
    return (
      <Col xs={4} className="SettingsLineChart__col">
        <FormGroup className="SettingsLineChart__form-group">
          <ControlLabel>
            <FormattedMessage id="plugin.chart.Y2axis" />
            <FieldLevelHelp
              content={<FormattedMessage id="plugin.chart.Y2axis.help" />}
              close="true"
            />
          </ControlLabel>
          <Field
            component={SwitchRenderer}
            name="axis.y2.show"
            className="pull-right"
          />
        </FormGroup>
        {wrapInputTextField(
          "axis.y2.label",
          "plugin.chart.labelY2axis",
          "plugin.table.requirement",
          !this.props.axisY2Show
        )}

        <FieldArray
          className="SettingsLineChart__column-selected"
          name="columns.y2"
          component={FieldArrayDropDownMultiple}
          optionColumns={optionColumns}
          optionColumnYSelected={optionColumnY2Selected}
        />
      </Col>
    );
  }

  render() {
    return (
      <div className="SettingsLineChart">
        <Row>
          <Col xs={12}>
            <FormGroup className="SettingsLineChart__input-group">
              <div className="checkbox">
                <label>
                  <Field
                    component="input"
                    type="checkbox"
                    name="axis.rotated"
                  />
                  <strong>
                    <FormattedMessage id="plugin.chart.invertAxisXtoY" />
                  </strong>
                </label>
              </div>
            </FormGroup>
          </Col>
        </Row>
        <Row>
          {this.rederAxisX()}
          {this.rederAxisY()}
          {this.rederAxisY2()}
        </Row>
      </div>
    );
  }
}
export default SettingsLineChart;