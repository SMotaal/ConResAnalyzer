classdef UniformityPlotComponent < Grasppe.Core.Prototype & Grasppe.Core.MouseEventHandler
  %UNIFORMITYPLOTOBJECT Co-superclass for printing uniformity plot objects
  %   Detailed explanation goes here
  
  properties (SetObservable, GetObservable)
    DataSource
    LinkedProperties
    
  end
  
  properties (Transient, Hidden)
    
    UniformityPlotComponentProperties = {
      'ALim',       'Alpha Map Limits', 'Data Limits',      'limits',   '';   ...
      'CLim',       'Color Map Limits', 'Data Limits',      'limits',   '';   ...
      'XLim',       'X Axes Limits',    'Data Limits',      'limits',   '';   ...
      'YLim',       'Y Axes Limits',    'Data Limits',      'limits',   '';   ...
      'ZLim',       'Z Axes Limits',    'Data Limits',      'limits',   '';   ...
      };%     HANDLEPROPERTIES  = {};
  end
  
  
  properties (Dependent)
    %     IsLinked;
  end
  
  methods
    function obj = UniformityPlotComponent(dataSource, varargin)
      obj = obj@Grasppe.Core.Prototype; %Component(varargin{:}, 'DataSource', dataSource);
      obj.DataSource = dataSource;
    end
  end
  
  methods (Access=protected)
    
    %     function createComponent(obj, type)
    % %       if ~UniformityPlotObject.checkInheritence(obj.DataSource, 'UniformityDataSource')
    % %         obj.DataSource = RawUniformityDataSource.Create(obj);
    % %       end
    % %
    %
    %     end
    
    function attachDataSource(obj)
      obj.resetPlotLimits;
      obj.DataSource.attachPlotObject(obj);
      
      if isempty(obj.ParentFigure.DataSources) || ~iscell(obj.ParentFigure.DataSources)
        obj.ParentFigure.DataSources = {};
      end
      
      obj.ParentFigure.DataSources{end+1} = obj.DataSource;
    end
    
    function resetPlotLimits(obj)
      try obj.ParentAxes.XLim = 'auto'; end
      try obj.ParentAxes.YLim = 'auto'; end
      try obj.ParentAxes.ZLim = 'auto'; end
      try obj.ParentAxes.CLim = 'auto'; end
    end
  end
  
  methods
    function set.DataSource(obj, value)
      try obj.DataSource = value; end
      value.attachPlotObject(obj);
      %       try value.attachPlotObject(obj); end
    end
        
    function setSheet(obj, varargin)
      try obj.DataSource.setSheet(varargin{:}); end
    end
    
  end
  
  
  methods (Hidden)
    function OnMouseScroll(obj, source, event)
      %       if ~isequal(obj.Handle, hittest)
      %         consumed = false;
      %         return;
      %       end
      
      if ~event.Data.Scrolling.Momentum
        % disp(toString(event));
        % plotAxes = get(obj.handleGet('CurrentAxes'), 'UserData');
        if event.Data.Scrolling.Vertical(1) > 0
          obj.setSheet('+1');
        elseif event.Data.Scrolling.Vertical(1) < 0
          obj.setSheet('-1');
        end
      end
    end    
  end
  
end

