import "test/enzyme-init";
import {shallow} from "enzyme";

import React from "react";
import DashboardConfigEditPage from "ui/dashboard-config/add/components/DashboardConfigEditPage";
import {Row, Grid, Breadcrumb} from "patternfly-react";
import PageTitle from "ui/PageTitle";
import DashboardConfigFormContainer from "ui/dashboard-config/common/containers/DashboardConfigFormContainer";

describe("DashboardConfigEditPage", () => {
  let component;
  beforeEach(() => {
    component = shallow(<DashboardConfigEditPage />);
  });

  it("renders without crashing", () => {
    expect(component.exists()).toBe(true);
  });

  it("have one Grid ", () => {
    expect(component.find(Grid)).toHaveLength(1);
  });

  it("have one Breadcrumb ", () => {
    expect(component.find(Breadcrumb)).toHaveLength(1);
  });

  it("have four Breadcrumb.Item ", () => {
    expect(component.find(Breadcrumb.Item)).toHaveLength(4);
  });

  it("have three Rows ", () => {
    expect(component.find(Row)).toHaveLength(3);
  });

  it("have one PageTitle ", () => {
    expect(component.find(PageTitle)).toHaveLength(1);
  });
  it("have one DashboardConfigFormContainer with prop edit", () => {
    expect(component.find(DashboardConfigFormContainer)).toHaveLength(1);
    expect(component.find(DashboardConfigFormContainer).prop("mode")).toBe(
      "edit"
    );
  });
});