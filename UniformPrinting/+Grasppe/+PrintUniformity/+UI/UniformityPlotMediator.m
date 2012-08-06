classdef UniformityPlotMediator < Grasppe.PrintUniformity.UI.PlotMediator
  %UNIFORMITYPLOTMEDIATOR Summary of this class goes here
  %   Detailed explanation goes here
  
  properties (Access=private)
    FigureOptions   = {'PlotAxesLength', 3};
    PlotOptions     = {'CaseID', 'rithp5501'};
    PlotTypes       = {'Surface', 'Regions', 'Slope'};
    DataProperties  = {'CaseID', 'SetID', 'SheetID'};    
    AxesProperties  = {'View', 'Projection', {'PlotColor', 'Color'}};
  end
  
  methods
    
    function obj = UniformityPlotMediator
      obj = obj@Grasppe.PrintUniformity.UI.PlotMediator;
          
      obj.createFigure(obj.FigureOptions{:}, 'IsVisible', false);
      
      obj.PlotFigure.PlotMediator = obj;
      
      mPos = get(0,'MonitorPositions');
      fPos = [mPos(1,1) mPos(4) mPos(3) 550];
      
      obj.PlotFigure.handleSet('Position', fPos);
      
      plotTypes   = obj.PlotTypes;
      plotAxes    = obj.PlotFigure.PlotAxes;
      plotOptions = obj.PlotOptions;
      for m = 1:numel(plotTypes)
        obj.createPlot(plotTypes{m}, plotAxes{m}, [], plotOptions{:});
      end
      
      obj.attachMediations;
      
    end
    
    function showFigure(obj)
      obj.PlotFigure.show;
    end
    
    function createFigure(obj, varargin)
      obj.PlotFigure = Grasppe.PrintUniformity.Graphics.UniformityPlotFigure(varargin{:}); 
    end
    
    function createPlot(obj, plotType, plotAxes, dataSource, varargin)
      import Grasppe.PrintUniformity.Data.*;
      
      createSource = nargin<4 || isempty(dataSource);
      switch lower(plotType)
        
        case {'uniformitysurface', 'surface', 'surf'}
          
          if createSource, dataSource  = UniformityPlaneDataSource(varargin{:}); end
          plotObject    = Grasppe.PrintUniformity.Graphics.UniformitySurf(plotAxes, dataSource);
          
        case {'localvariability', 'slope'}

          if createSource, dataSource  = LocalVariabilityDataSource(varargin{:}); end
          plotObject    = Grasppe.PrintUniformity.Graphics.UniformitySurf(plotAxes, dataSource);          
          
        case {'regions', 'region'}

          if createSource, dataSource  = RegionStatsDataSource(varargin{:}); end
          plotObject    = Grasppe.PrintUniformity.Graphics.UniformitySurf(plotAxes, dataSource);          
          
        otherwise % {'raw', 'scatter'}

          if createSource, dataSource  = RawUniformityDataSource(varargin{:}); end
          plotObject    = Grasppe.PrintUniformity.Graphics.UniformitySurf(plotAxes, dataSource);          
          
      end
      
      if createSource
        obj.DataSources = {obj.DataSources{:}, dataSource};
      end
      
      obj.PlotObjects = {obj.PlotObjects{:}, plotObject};
    end
    
    function executeCommand(obj, command)
      try eval(command); end
    end
    
    
    function attachMediations(obj)
      try 
        obj.PlotFigure.prepareMediator(obj.DataProperties, obj.AxesProperties);
      end
    end
  end
  
end

