classdef UniformityDataSource < Grasppe.Core.Component % & GrasppeComponent
  %UNIFORMITYDATASOURCE Superclass for surface uniformity data sources
  %   Detailed explanation goes here
  
  %   properties (Access=private)
  %     COMPONENTTYPE     = 'PrintingUniformityDataSource';
  %     HANDLEEVENTS      = {};
  %     DATAPROPERTIES    = {'CaseID', 'SetID', 'XData', 'YData', 'ZData', 'SheetID'};
  %     TESTPROPERTY      = 'test';
  %   end
 
  properties (Transient, Hidden)
    HandleProperties = {};
    HandleEvents = {};
    ComponentType = 'PrintingUniformityDataSource';
    ComponentProperties = '';
    
    DataProperties = {'CaseID', 'SetID', 'XData', 'YData', 'ZData', 'SheetID'};
    
    UniformityDataSourceProperties = {
      'CaseID',     'Case ID',          'Data Source',      'string',   '';   ...
      'SetID',      'Set ID',           'Data Source',      'int',      '';   ...
      'SheetID',    'Sheet ID',         'Data Source',      'int',      '';   ...
      'VariableID', 'Variable ID',      'Data Source',      'string',   '';   ...
      'PlotType',   'Plot Type',        'Data Processing'   'string',   '';   ...
      'ALim',       'Alpha Map Limits', 'Data Limits',      'limits',   '';   ...
      'CLim',       'Color Map Limits', 'Data Limits',      'limits',   '';   ...
      'XLim',       'X Axes Limits',    'Data Limits',      'limits',   '';   ...
      'YLim',       'Y Axes Limits',    'Data Limits',      'limits',   '';   ...
      'ZLim',       'Z Axes Limits',    'Data Limits',      'limits',   '';   ...
      };
    
  end
  
  properties (Hidden)
    LinkedPlotObjects = [];
    LinkedPlotHandles = [];
    PlotObjects       = [];
  end
  
  properties (SetObservable, GetObservable)
        
    AspectRatio
    XData, YData, ZData, CData
    CLim,       ALim                      %CLimMode   ALimMode
    XLim,       XTick,      XTickLabel    %XLimMode   XTickMode,  XTickLabelMode
    YLim,       YTick,      YTickLabel    %YLimMode   YTickMode,  YTickLabelMode
    ZLim,       ZTick,      ZTickLabel    %ZLimMode   ZTickMode,  ZTickLabelMode
    
    PlotType = 'Surface';
  end
  
  properties (GetAccess=public, SetAccess=protected)
    DataProcessor;
  end
  
  properties (SetObservable, GetObservable, AbortSet)
    %Parameters, % Data
    CaseID,     SetID,    SheetID,    VariableID='raw'
    CaseName,   SetName,  SheetName
    SampleSummary = false
  end
  
  properties (Hidden)
    LastCaseID,   LastSetID,  LastSheetID,  LastVariableID
  end
  
  properties (Dependent, Hidden=true)
    CaseData,   SetData,  SheetData
  end
  
  events
    CaseChange
    SetChange
    SheetChange
    VariableChange
    PlotChange
    ProcessorChange
  end  
  
  methods (Hidden)
    function obj = UniformityDataSource(varargin)
      obj = obj@Grasppe.Core.Component(varargin{:});
      
      args = varargin;
      plotObject = [];
      try
        if Grasppe.Graphics.PlotComponent.checkInheritence(varargin{1})
          plotObject = varargin{1};
          args = varargin(2:end);
        end
      end
      
      % obj.currentParameters = Grasppe.PrintUniformity.Models.DataParameters;
      
      % obj.updateSourceParameters();
      
      obj.attachPlotObject(plotObject);
      
    end
    
    function attachPlotObject(obj, plotObject)
      
      if isempty(plotObject) || ~Grasppe.Graphics.PlotComponent.checkInheritence(plotObject)
        return;
      end
      
      try debugStamp(obj.ID); catch, debugStamp(); end;
      plotObjects = obj.PlotObjects;
      if ~any(plotObjects==plotObject)
        try
          obj.PlotObjects(end+1) = plotObject;
        catch
          obj.PlotObjects = plotObject;
        end
      end
      
      obj.linkPlot(plotObject);
      
      obj.optimizeSetLimits;
      
    end
    
    function linkPlot(obj, plotObject)
      try
        if Grasppe.Graphics.PlotComponent.checkInheritence(plotObject)
          plotObject.XData    = 'xData'; ...
            plotObject.YData  = 'yData'; ...
            plotObject.ZData  = 'zData'; % ...
            % plotObject.CData  = 'cData';
            
          try
            obj.LinkedPlotObjects(end+1) = plotObject;
          catch
            obj.LinkedPlotObjects = plotObject;
          end
        end
      end
      obj.validatePlots();
      obj.updatePlots(plotObject.Handle);
    end
    
    function validatePlots(obj)
      try obj.LinkedPlotObjects = unique(obj.LinkedPlotObjects); end
      try obj.LinkedPlotHandles = unique([obj.LinkedPlotObjects.Handle]); end
    end
    
    function updatePlots(obj, linkedPlots)
      xData = obj.XData; yData = obj.YData; zData = obj.ZData; 

      % cData = obj.CData;
      
      linkedPlots = [];
      
      try
        linkedPlots = unique(linkedPlots);
        if exist('plotObject', 'var') && ...
            Grasppe.Graphics.PlotComponent.checkInheritence(plotObject)
          linkedPlots = linkedPlots.Handle;
        end
      catch err
      end
      
        if isempty(linkedPlots)
          obj.validatePlots;
          linkedPlots = obj.LinkedPlotHandles;
        end
      
      linkedPlots = linkedPlots(ishandle(obj.LinkedPlotHandles));
      try
        refreshdata(linkedPlots, 'caller');
        % disp(['Refreshing Data for ' toString(linkedPlots(:))]);
      catch err
        disp(['Refreshing Data FAILED for ' toString(linkedPlots(:))]);
        % halt(err, 'obj.ID');
        try debugStamp(obj.ID, 4); end
      end
      
      for h = linkedPlots
        plotObject = get(h, 'UserData');
        try plotObject.refreshPlot(obj); end
        % try plotObject.updatePlotTitle; end
      end
    end
    
  end
  
  methods (Access=protected)
    
    function createComponent(obj)
      obj.initializeDataProcessor;
      obj.createComponent@Grasppe.Core.Component;
    end
    
    function initializeDataProcessor(obj)
      if ~isa(obj.DataProcessor, 'Grasppe.PrintUniformity.Data.UniformityProcessor') ...
          || ~isvalid(obj.DataProcessor)
        obj.DataProcessor = Grasppe.PrintUniformity.Data.UniformityProcessor;
        obj.DataProcessor.addlistener('CaseChange',   @obj.updateCaseData);
        obj.DataProcessor.addlistener('SetChange',    @obj.updateSetData);
        obj.DataProcessor.addlistener('SheetChange',  @obj.updateSheetData);
        obj.DataProcessor.addlistener('VariableChange',  @obj.updateVariableData);
      end
    end
  end
  
  %% Data Source Getters & Setters
  methods
    
    %% currentParameters
    
    %     function parameters = get.currentParameters(obj)
    %       parameters = [];
    %       try
    % %         if isempty(obj.currentParameters)
    % %           obj.currentParameters = Grasppe.PrintUniformity.Models.DataParameters;
    % %         end
    %       end
    %       try parameters = obj.currentParameters; end
    %     end
    
    %% CaseID / CaseName / CaseData
    function caseID = get.CaseID(obj)
      caseID = [];
      try caseID = obj.DataProcessor.CaseID; end
    end
    
    function set.CaseID(obj, caseID)
      processor = obj.DataProcessor;
      
      lastValue       = obj.LastCaseID;
      obj.LastCaseID  = obj.CaseID;
      
      [processor.CaseID changed] = changeSet(processor.CaseID, caseID);
      
      if changed
        obj.CaseID      = processor.CaseID; 
      else
        obj.LastCaseID  = lastValue;
      end

    end
    
    function caseName = get.CaseName(obj)
      caseName = []; pressName = []; runCode = [];
      
      try pressName = obj.CaseData.metadata.testrun.press.name; end
      
      try runCode   = obj.CaseData.name; end
      try runCode   = sprintf('#%s', char(regexpi(runCode, '[0-9]{2}[a-z]?$', 'match'))); end
      
      try caseName  = strtrim([pressName ' ' runCode]); end
    end
    
    function caseData = get.CaseData(obj)
      caseData = [];
      try caseData = obj.DataProcessor.CaseData; end
    end
    
    %% SetID & SetName
    function setID = get.SetID(obj)
      setID = [];
      try setID = obj.DataProcessor.SetID; end
    end
    
    function set.SetID(obj, setID)
      processor = obj.DataProcessor;
      
      lastValue     = obj.LastSetID;
      obj.LastSetID = obj.SetID;
      
      [processor.SetID changed] = changeSet(processor.SetID, setID);
      
      if changed
        obj.SetID     = processor.SetID; 
      else
        obj.LastSetID = lastValue;
      end
      
    end
    
    function setName = get.SetName(obj)
      setName = [];
      %try setName = obj.SetData.setLabel; end
      try
        setName = [int2str(obj.SetData.patchSet) '%'];
      end
      if isnumeric(setName), setName = int2str(setName); end
    end
    
    function setData = get.SetData(obj)
      setData = [];
      try setData = obj.DataProcessor.SetData; end
    end    
    
    %% SheetID & SheetName
    function sheetID = get.SheetID(obj)
      sheetID = [];
      try sheetID = obj.DataProcessor.SheetID; end
    end
    
    function set.SheetID(obj, sheetID)
      processor = obj.DataProcessor;
      
      lastValue       = obj.LastSheetID;
      obj.LastSheetID = obj.SheetID;
      
      [processor.SheetID changed] = changeSet(processor.SheetID, sheetID);
      
      if changed
        obj.SheetID     = processor.SheetID; 
      else
        obj.LastSheetID = lastValue;
      end
    end
    
    function sampleName = get.SheetName(obj)
      sampleName = [];
      try sampleName = obj.CaseData.index.Sheets(obj.SheetID); end
      if isnumeric(sampleName), sampleName = int2str(sampleName); end
    end
    
    function sheetData = get.SheetData(obj)
      sheetData = [];
      try sheetData = obj.DataProcessor.SheetData; end
    end    
    
    %% VariableID
    function variableID = get.VariableID(obj)
      variableID = [];
      try variableID = obj.DataProcessor.VariableID; end
    end
    
    function set.VariableID(obj, variableID)
      processor = obj.DataProcessor;
      
      lastValue           = obj.LastVariableID; %obj.VariableID;
      obj.LastVariableID  = obj.VariableID;
      
      [processor.VariableID changed] = changeSet(processor.VariableID, variableID);
      
      if changed
        obj.VariableID      = processor.VariableID;
      else
        obj.LastVariableID  = lastValue;
      end
    end
    
    
    %% SetCount, SheetCount, Rows, Columns, RegionCount, ZoneCount
    
    function sets = getSetCount(obj)
      sets = [];
      try sets = obj.CaseData.Datasets.Length; end
    end
    
    function samples = getSheetCount(obj)
      samples = [];
      try samples = obj.CaseData.length.Sheets; end
    end
    
    function rows = getRowCount(obj)
      rows = [];
      try rows = obj.CaseData.metrics.sampleSize(1); end
    end
    
    function columns = getColumnCount(obj)
      columns = [];
      try columns = obj.CaseData.metrics.sampleSize(2); end
    end
    
%     function regions = getRegionCount(obj)
%       regions = [];
%       try regions = obj.CaseData.metrics.sampleSize(1); end
%     end
%     
%     function zones = getZoneCount(obj)
%       zones = [];
%       try zones = obj.CaseData.metrics.sampleSize(1); end
%     end
    
  end
  
  %% Data Source Update Routines
  methods
    
    % function updateSourceParameters(obj)
    %   try debugStamp(obj.ID); catch, debugStamp(); end;
    %
    %   if ~isa(obj.currentParameters, 'Grasppe.PrintUniformity.Models.DataParameters')
    %     return;
    %   end
    %
    %   parameters = obj.currentParameters;
    %
    %   [parameters.CaseID      updateSource    ] = changeSet(parameters.CaseID,      obj.CaseID      );
    %   [parameters.SetID       updateSet       ] = changeSet(parameters.SetID,       obj.SetID       );
    %   [parameters.SheetID     updateSheet     ] = changeSet(parameters.SheetID,     obj.SheetID     );
    %   [parameters.VariableID  updateVariable  ] = changeSet(parameters.VariableID,  obj.VariableID  );
    %
    %   if updateSource || updateSet || updateVariable
    %     obj.updateCaseData();
    %   end
    %
    %   if updateSheet || updateSet || updateSource || updateVariable
    %     obj.updateSheetData();
    %   end
    %
    %   [obj.CaseID     updateSource    ] = changeSet(obj.CaseID,     parameters.CaseID     );
    %   [obj.SetID      updateSet       ] = changeSet(obj.SetID,      parameters.SetID      );
    %   [obj.SheetID    updateSheet     ] = changeSet(obj.SheetID,    parameters.SheetID    );
    %   [obj.VariableID updateVariable  ] = changeSet(obj.VariableID, parameters.VariableID );
    % end
    
    function updateCaseData(obj, source, event)

      try
        obj.notify('CaseChange', event.EventData); 
      catch err
        obj.notify('CaseChange');
      end
      
      obj.updateSetData(source, event);      
      
    end
    
    function updateSetData(obj, source, event)
      % dispf('%s.%s', obj.ID, 'updateSetData');
      
      try
        obj.notify('SetChange', event.EventData); 
      catch err
        obj.notify('SetChange');
      end
      
      obj.optimizeSetLimits;
      
      obj.updateSheetData(source, event);
      
    end
    
    function updateSheetData(obj, source, event)

      processor     = obj.DataProcessor; ...
        ...
        sourceID    = processor.CaseID; ...
        setID       = processor.SetID; ...
        sheetID     = processor.SheetID; ...
        variableID  = processor.VariableID;
      
      if isempty(sheetID) || ~isnumeric(sheetID), return; end
      
      try
        obj.notify('SheetChange', event.EventData); 
      catch err
        obj.notify('SheetChange');
      end      
      
      rows      = obj.getRowCount;
      columns   = obj.getColumnCount;
      
      [X Y Z]   = obj.processSheetData(sheetID, variableID);
      
      obj.setPlotData(X, Y, Z);     
      
    end
    
    function updateVariableData(obj, source, event)
      try
        obj.notify('VariableChange', event.EventData); 
      catch err
        obj.notify('VariableChange');
      end
      
      obj.updateSheetData(source, event);
    end
    
    function [X Y Z] = processSheetData(obj, sheetID, variableID)
      rows    = obj.getRowCount;
      columns = obj.getColumnCount;
      
      [X Y Z] = meshgrid(1:columns, 1:rows, 1);
    end
    
    function setPlotData(obj, XData, YData, ZData)
      try debugStamp(obj.ID); catch, debugStamp(); end;
      obj.XData = XData;
      obj.YData = YData;
      obj.ZData = ZData;
      obj.CData = ZData;
      
      obj.updatePlots();
    end
    
    
    function optimizeSetLimits(obj, x, y, z, c)
      %% Optimize XLim & YLim
      xLim = 'auto';
      yLim = 'auto';
      
      try
        if nargin > 1 && ~isempty(x) % isnumeric(x) && size(x)==[1 2];
          xLim = x;
        else
          xLim = [1 obj.getColumnCount];
        end
      end
        
      try
        if nargin > 2 && ~isempty(y) % isnumeric(y) && size(y)==[1 2];
          yLim = y;
        else
          yLim = [1 obj.getRowCount];
        end
      end
      
      obj.XLim  = xLim;
      obj.YLim  = yLim;
      
      
      %% Optimize ZLim & CLim
      zLim = 'auto';
      cLim = 'auto';
      
      try
        if nargin > 3 && ~isempty(z) % isnumeric(z) && size(z)==[1 2];
          zLim = z;
        else
          setData   = obj.SetData;
          
          zData     = [setData.data(:).zData];
          zMean     = nanmean(zData);
          zStd      = nanstd(zData,1);
          zSigma    = [-3 +3] * zStd;
          
          
          zMedian   = round(zMean*2)/2;
          zRange    = [-3 +3];
          zLim      = zMedian + zRange;
        end
      end
      
      try
        if nargin > 4 && ~isempty(c) % isnumeric(c) && size(c)==[1 2];
          cLim      = c;
        else
          cLim      = zLim;
        end
      end
      
      obj.ZLim  = zLim;
      obj.CLim  = cLim;
      
      %% Update to LinkedPlots
      try 
        plotObject = obj.LinkedPlotObjects;

        for m = 1:numel(plotObject)
          try plotObject(m).ParentAxes.XLim = obj.XLim; end
          try plotObject(m).ParentAxes.YLim = obj.YLim; end
          try plotObject(m).ParentAxes.ZLim = obj.ZLim; end
          try plotObject(m).ParentAxes.CLim = obj.CLim; end        
        end
      end
      
    end
    
    
    function sheet = setSheet (obj, sheet)
      
      try debugStamp(obj.ID); catch, debugStamp(); end;
      
      currentSheet  = obj.SheetID;
      firstSheet    = 1;
      lastSheet     = obj.getSheetCount;
      nextSheet     = currentSheet;
      
      %Parse sheet
      if isInteger(sheet)
        nextSheet = sheet;
      else
        step = 0;
        switch lower(sheet)
          case {'summary', 'sum'}
            if isequal(obj.SampleSummary, true), nextSheet = 0; end
          case {'alpha', 'first', '#1'}
            nextSheet = firstSheet;
          case {'omega', 'last'}
            nextSheet = lastSheet;
          case {'forward',  'next', '+1', '<'}
            step = +1;
          case {'previous', 'back', '-1', '>'}
            step = -1;
          otherwise
            try
              switch(sheet(1))
                case '+'
                  step = +str2double(sheet(2:end));
                case '-'
                  step = -str2double(sheet(2:end));
              end
            end
        end
        if ~isequal(step, 0)
          nextSheet = stepSet(currentSheet, step, lastSheet, 1);
        end
      end
      
      % dispf('Sheet #%d >> %s', round(nextSheet), obj.ID);
      obj.SheetID = nextSheet;
      
    end
    
  end
  
  %% Static Component Methods
  
  methods(Static, Hidden)
    function OPTIONS  = DefaultOptions()
      Grasppe.Utilities.DeclareOptions;
    end
  end
  
  
end
