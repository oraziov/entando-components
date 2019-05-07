import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Field, FormSection } from 'redux-form';
import { maxLength } from '@entando/utils';
import FormattedMessage from 'ui/i18n/FormattedMessage';
import { OverlayTrigger, Tooltip } from 'patternfly-react';
import { get } from 'lodash';

import {
  SortableContainer,
  SortableElement,
  sortableHandle,
  arrayMove,
} from 'react-sortable-hoc';

const maxLength15 = maxLength(15);

const renderField = ({ input, meta: { touched, error } }) => {
  const classContainer =
    touched && error ?
      'DashboardTableColumns__container-input--error' :
      'DashboardTableColumns__container-input';

  return (
    <div className={classContainer}>
      <input id={input.name} {...input} type="text" />
      {touched && (error && <span className="help-block">{error}</span>)}
    </div>
  );
};

const DragHandle = sortableHandle(() => (
  <div className="DashboardTableColumns__th-editable-label-dnd" />
));

const SortableItem = SortableElement(({ item, fieldColumnData }) => (
  <th className="DashboardTableColumns__th-editable-label">
    <div className="DashboardTableColumns__th-editable-label-container">
      <DragHandle />
      <Field
        component={renderField}
        name={`${item.key}.label`}
        validate={[maxLength15]}
      />
      <div className="DashboardTableColumns__th-editable-label-visible">
        <OverlayTrigger
          overlay={
            <Tooltip id={`${item.key}-overlay`}>
              {get(get(fieldColumnData, item.key), 'hidden') ? (
                <FormattedMessage id="plugin.table.column.tooltip.show" />
              ) : (
                <FormattedMessage id="plugin.table.column.tooltip.hidden" />
              )}
            </Tooltip>
          }
          placement="top"
          trigger={['hover']}
          rootClose={false}
        >
          <Field
            component="input"
            type="checkbox"
            id={`${item.key}.hidden`}
            name={`${item.key}.hidden`}
            className="DashboardTableColumns__th-column-is-hidden"
          />
        </OverlayTrigger>
      </div>
      <div className="DashboardTableColumns__th-editable-type-date">
        <Field
          component="input"
          type="checkbox"
          id={`${item.key}.type`}
          name={`${item.key}.isDate`}
        />
        <label htmlFor={`${item.key}.type`}>
          <FormattedMessage id="plugin.table.column.isDate" />
        </label>
      </div>
    </div>
  </th>
));

const SortableList = SortableContainer(({ items, fieldColumnData }) => (
  <FormSection name="columns">
    <div className="DashboardTableColumns__container-columns">
      <label htmlFor="label">
        {items.length > 0 ? (
          <FormattedMessage id="plugin.table.column.default.label" />
          ) : null}
      </label>
      <table className="DashboardTableColumns__table table table-striped table-bordered">
        <thead>
          <tr>
            {items.map(item => (
              <th
                key={`item-${item.key}`}
                className="DashboardTableColumns__th-default-label"
              >
                {item.key}
              </th>
              ))}
          </tr>
        </thead>
      </table>
      <label htmlFor="label">
        {items.length > 0 ? (
          <FormattedMessage id="plugin.table.column.customizable.label" />
          ) : null}
      </label>
      <table className="DashboardTableColumns__table table table-striped table-bordered">
        <thead>
          <tr>
            {items.map((item, index) => (
              <SortableItem
                key={`item-${item.key}`}
                index={index}
                item={item}
                fieldColumnData={fieldColumnData}
              />
              ))}
          </tr>
        </thead>
      </table>
    </div>
  </FormSection>
));

class DashboardTableColumns extends Component {
  constructor(props) {
    super(props);
    this.onMoveHandler = this.onMoveHandler.bind(this);
  }

  onMoveHandler({ oldIndex, newIndex }) {
    const { onMoveColumn, columns } = this.props;
    onMoveColumn(arrayMove(columns, oldIndex, newIndex));
  }

  render() {
    return (
      <div className="DashboardTableColumns">
        <SortableList
          items={this.props.columns}
          fieldColumnData={this.props.fieldColumnData}
          // showColumnHandler={this.props.onShowHideColumn}
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
  hidden: PropTypes.bool,
};

DashboardTableColumns.propTypes = {
  columns: PropTypes.arrayOf(PropTypes.shape(COLUMN_TYPE)),
  fieldColumnData: PropTypes.shape({}),
  onMoveColumn: PropTypes.func.isRequired,
};

DashboardTableColumns.defaultProps = {
  columns: [],
  fieldColumnData: undefined,
};

renderField.propTypes = {
  input: PropTypes.shape({}).isRequired,
  meta: PropTypes.shape({}).isRequired,
};

export default DashboardTableColumns;
