import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { reduxForm } from 'redux-form';

import Stepper from 'ui/widgets/common/components/Stepper';
import ChartFirstStepContent from 'ui/widgets/charts/common/components/ChartFirstStepContent';
import ChartSecondStepContent from 'ui/widgets/charts/common/components/ChartSecondStepContent';
import ChartThirdStepContent from 'ui/widgets/charts/common/components/ChartThirdStepContent';

const data = { columns: [['data1', 30], ['data2', 120]] };

const FORM_NAME = 'form-dashboard-donut-chart';
const TYPE_CHART = 'DONUT_CHART';
const CHART_PREVIEW = 'Donut Chart';

export class DashboardDonutChartFormBody extends Component {
  componentWillMount() {
    this.props.onWillMount();
  }

  render() {
    const { formSyncErrors, chart } = this.props;
    const validateSteps = [false, false, false];
    if (
      !formSyncErrors.title &&
      !formSyncErrors.serverName &&
      !formSyncErrors.datasource
    ) {
      validateSteps[0] = true;
    }
    if (!formSyncErrors.columns) {
      validateSteps[1] = true;
    }

    return (
      <Stepper
        handleSubmit={this.props.handleSubmit}
        onCancel={this.props.onCancel}
        validateSteps={validateSteps}
        step1={<ChartFirstStepContent formName={FORM_NAME} />}
        step2={
          <ChartSecondStepContent
            typeChart={TYPE_CHART}
            data={data.columns}
            labelChartPreview={CHART_PREVIEW}
            formName={FORM_NAME}
          />
        }
        step3={
          <ChartThirdStepContent
            typeChart={TYPE_CHART}
            data={data.columns}
            labelChartPreview={CHART_PREVIEW}
            formName={FORM_NAME}
            chart={chart}
          />
        }
      />
    );
  }
}
DashboardDonutChartFormBody.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
  onWillMount: PropTypes.func.isRequired,
  onCancel: PropTypes.func.isRequired,
  chart: PropTypes.string.isRequired,
  formSyncErrors: PropTypes.shape({}),
};

DashboardDonutChartFormBody.defaultProps = {
  formSyncErrors: {},
};

const DashboardDonutChartForm = reduxForm({
  form: FORM_NAME,
})(DashboardDonutChartFormBody);

export default DashboardDonutChartForm;