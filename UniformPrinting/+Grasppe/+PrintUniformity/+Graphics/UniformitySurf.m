classdef UniformitySurf < Grasppe.Graphics.Surf & Grasppe.PrintUniformity.Graphics.UniformityPlotComponent
  %UNIFORMITYSURFACEPLOT Summary of this class goes here
  %   Detailed explanation goes here
  
  properties
    ExtendedDataProperties = {};
  end
  
  properties (Dependent)
  end
  
  methods
    function obj = UniformitySurf(parentAxes, dataSource, varargin)
      obj = obj@Grasppe.Graphics.Surf(parentAxes, varargin{:});
      obj = obj@Grasppe.PrintUniformity.Graphics.UniformityPlotComponent(dataSource);
      
      obj.attachDataSource;
    end
    
  end
  
  methods (Access=protected)
    
    function createComponent(obj)
      try
        if ~(isempty(obj.Handle) || ~isvalid(obj.Handle)), return; end;
        obj.createComponent@Grasppe.Graphics.Surf();
      end
      % obj.createComponent@Grasppe.PrintUniformity.Graphics.UniformityPlotComponent();
      
      % obj.ParentFigure.registerMouseEventHandler(obj);
      obj.ParentAxes.AspectRatio = [20 20 1];
      % obj.handleSet('EdgeAlpha', 0.5);
      % obj.handleSet('LineWidth', 0.25);
    end
  end
  
  
  methods
    function refreshPlot(obj, dataSource)
      
      % if ~obj.HasParentAxes, return; end
      
      try obj.ParentAxes.ZLim = dataSource.ZLim; end
      try obj.ParentAxes.CLim = dataSource.CLim; end
      
      %try obj.ParentFigure.SampleTitle = dataSource.SheetName; end
      obj.updatePlotTitle;
      
    end
    
    function updatePlotTitle(obj, base, sample)
      
      try caseName  = obj.DataSource.CaseName;  end
      try setName   = obj.DataSource.SetName;   end
      try sheetName = obj.DataSource.SheetName; end
      
      try obj.ParentFigure.BaseTitle    = [caseName ' ' setName]; end;
      try obj.ParentFigure.SampleTitle  = sheetName; end;
    end    
    
    function refreshPlotData(obj, source, event)
      try debugStamp(obj.ID); catch, debugStamp(); end;
      try
        dataSource  = event.AffectedObject;
        dataField   = source.Name;
        
        % dispf('Refreshing %s.%s', dataSource.ID, dataField);
        
        obj.handleSet(dataField, dataSource.(dataField));
      catch err
        try debugStamp(obj.ID); end
        disp(err);
      end
    end
    
    
    function consumed = mouseWheel(obj, source, event)
      consumed = true;
      
      if ~obj.HasParentFigure || ~obj.HasParentAxes || event.consumed
        consumed = event.consumed;
        return;
      end
      
      % disp([hittest obj.Handle obj.ParentAxes.Handle]);
      
      if ~isequal(obj.Handle, hittest)
        consumed = false;
        return;
      end
      
      %       currentObject   = obj.ID;
      %       activeObject    = class(obj.ParentFigure.ActiveObject);
      %       try activeObject  = obj.ParentFigure.ActiveObject.ID; end
      %
      %       if ~isequal(currentObject, activeObject)
      %         %disp(['Current object ' currentObject ' is not the Active object ' activeObject '.']);
      %         consumed = false;
      %         return;
      %       else
      %         %disp(['Current object ' currentObject ' is the Active object.']);
      %       end
      
      if ~event.Scrolling.Momentum
        % disp(toString(event));
        % plotAxes = get(obj.handleGet('CurrentAxes'), 'UserData');
        if event.Scrolling.Vertical(1) > 0
          obj.setSheet('+1');
        elseif event.Scrolling.Vertical(1) < 0
          obj.setSheet('-1');
        end
      end
    end
    
  end
  
  methods (Static)
    %     function obj = Create(parentAxes, varargin)
    %       obj = UniformitySurfaceObject(parentAxes, varargin{:});
    %     end
  end
  
  
  methods (Static, Hidden)
    function OPTIONS  = DefaultOptions( )
      
      IsVisible     = true;
      IsClickable   = true;
      EdgeColor     = 'none';
      Clipping      = 'off';
      
      Grasppe.Utilities.DeclareOptions;
    end
    
  end
  
end

