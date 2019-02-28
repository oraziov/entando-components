import React, {Component} from "react";
import PropTypes from "prop-types";
import {Field, FormSection} from "redux-form";
import {maxLength} from "@entando/utils";
import FormattedMessage from "ui/i18n/FormattedMessage";
import {OverlayTrigger, Tooltip} from "patternfly-react";

import {
  SortableContainer,
  SortableElement,
  sortableHandle,
  arrayMove
} from "react-sortable-hoc";

const maxLength15 = maxLength(15);

const renderField = ({input, meta: {touched, error}}) => {
  const classContainer =
    touched && error
      ? "DashboardTableColumns__container-input--error"
      : "DashboardTableColumns__container-input";

  return (
    <div className={classContainer}>
      <input id={input.name} {...input} type="text" />
      {touched && (error && <span className="help-block">{error}</span>)}
    </div>
  );
};

const renderHiddenColum = ({input}) => {
  return (
    <input
      id={input.name}
      {...input}
      type="checkbox"
      className={"DashboardTableColumns__th-column-is-hidden"}
    />
  );
};

const DragHandle = sortableHandle(() => (
  <div className="DashboardTableColumns__th-editable-label-dnd" />
));

const SortableItem = SortableElement(({item, showColumnHandler}) => (
  <th className="DashboardTableColumns__th-editable-label">
    <div className="DashboardTableColumns__th-editable-label-container">
      <DragHandle />
      <Field
        component={renderField}
        name={`${item.key}.label`}
        validate={[maxLength15]}
      />
      <div className="DashboardTableColumns__th-editable-label-visible">
        {showColumn(item.key)}
      </div>
    </div>
  </th>
));

const showColumn = name => (
  <OverlayTrigger
    overlay={
      <Tooltip id={name}>
        <FormattedMessage
          id={`plugin.table.column.tooltip.${name} ? "show" : "hidden"`}
        />
      </Tooltip>
    }
    placement="top"
    trigger={["hover"]}
    rootClose={false}
  >
    <Field component={renderHiddenColum} name={`${name}.hidden`} />
  </OverlayTrigger>
);

const SortableList = SortableContainer(({items, showColumnHandler}) => {
  return (
    <FormSection name="columns">
      <div className="DashboardTableColumns__container-columns">
        <label>
          {items.length > 0 ? (
            <FormattedMessage id={"plugin.table.column.default.label"} />
          ) : null}
        </label>
        <table className="DashboardTableColumns__table table table-striped table-bordered">
          <thead>
            <tr>
              {items.map((item, index) => (
                <th
                  key={`item-${index}`}
                  className="DashboardTableColumns__th-default-label"
                >
                  {item.key}
                </th>
              ))}
            </tr>
          </thead>
        </table>
        <label>
          {items.length > 0 ? (
            <FormattedMessage id={"plugin.table.column.customizable.label"} />
          ) : null}
        </label>
        <table className="DashboardTableColumns__table table table-striped table-bordered">
          <thead>
            <tr>
              {items.map((item, index) => (
                <SortableItem
                  key={`item-${index}`}
                  index={index}
                  item={item}
                  showColumnHandler={showColumnHandler}
                />
              ))}
            </tr>
          </thead>
        </table>
      </div>
    </FormSection>
  );
});

class DashboardTableColumns extends Component {
  constructor(props) {
    super(props);
    this.onMoveHandler = this.onMoveHandler.bind(this);
  }

  onMoveHandler({oldIndex, newIndex}) {
    const {onMoveColumn, columns} = this.props;
    onMoveColumn(arrayMove(columns, oldIndex, newIndex));
  }

  render() {
    return (
      <div className="DashboardTableColumns">
        <SortableList
          items={this.props.columns}
          showColumnHandler={this.props.onShowHideColumn}
          lockAxis="x"
          axis="x"
          pressDelay={200}
          onSortEnd={this.onMoveHandler}
        />
      </div>
    );
  }
}

const COLUMN_TYPE = {
  key: PropTypes.string,
  hidden: PropTypes.bool
};

DashboardTableColumns.propTypes = {
  columns: PropTypes.arrayOf(PropTypes.shape(COLUMN_TYPE)),
  onMoveColumn: PropTypes.func.isRequired
};

DashboardTableColumns.defaultProps = {
  columns: []
};

export default DashboardTableColumns;