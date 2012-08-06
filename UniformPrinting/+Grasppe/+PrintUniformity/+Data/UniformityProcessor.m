classdef UniformityProcessor < Grasppe.Core.Component
  %UNIFORMITYPROCESSOR Summary of this class goes here
  %   Detailed explanation goes here
  
  
  properties (Transient, Hidden)
    HandleProperties = {};
    HandleEvents = {};
    ComponentType = 'PrintingUniformityDataProcessor';
    ComponentProperties = '';
    
    DataProperties = {'CaseID', 'SetID', 'SheetID'};
    
    UniformityProcessorProperties = {
      'CaseID',     'Case ID',          'Data Source',      'string',   '';   ...
      'SetID',      'Set ID',           'Data Source',      'int',      '';   ...
      'SheetID',    'Sheet ID',         'Data Source',      'int',      '';   ...
      'VariableID', 'Variable ID',      'Data Source',      'string',   '';   ...
      };
    
  end
  
  
  properties (AbortSet, Dependent)
    CaseID='', SetID=100, SheetID=1, VariableID='raw';
  end
  
  properties (Hidden)
    CaseData, SetData, SheetData
    
  end
  
  properties
    Data;
  end
  
  properties (GetAccess=public, SetAccess=protected)
    Parameters = [];
  end
  
  events
    CaseChange
    SetChange
    SheetChange
    VariableChange
  end
  
  methods
    
    function obj = UniformityProcessor(varargin)
      obj = obj@Grasppe.Core.Component(varargin{:});
    end
    
  end
  
  methods (Access=protected)
    
    function createComponent(obj)
      obj.initializeDataModels;
      obj.createComponent@Grasppe.Core.Component;
    end
  end
  methods;
    
    %% CaseID
    
    function set.CaseID(obj, caseID)
      import Grasppe.PrintUniformity.Data.*;
      import Grasppe.PrintUniformity.Models.*;
      
      % obj.initializeDataModels;
      
      parameters      = obj.Parameters;
      
      setID   = 100;
      try setID       = obj.MetaProperties.SetID.NativeMeta.DefaultValue; end
      try if ~isempty(parameters.SetID),      setID     = parameters.SetID; end; end
      
      if ~isequal(obj.Parameters.CaseID, caseID)
        obj.resetDataModels;
        
        obj.Parameters.CaseID     = caseID;
        obj.SetID                 = setID;
        
        obj.notify('CaseChange');
      end
      
    end
    
    function resetDataModels(obj)
      % obj.deleteDataModels;
      obj.initializeDataModels;
    end
    
    function deleteDataModels(obj)
      if isobject(obj.Data)
        delete(obj.Data);         obj.Data =[];
      end
      
      if isobject(obj.Parameters)
        delete(obj.Parameters);   obj.Parameters =[];
      end
    end
    
    function initializeDataModels(obj)
      if ~isa(obj.Data, 'Grasppe.PrintUniformity.Models.UniformityData')
        obj.Data  = Grasppe.PrintUniformity.Models.UniformityData('Creator', obj);
      end
      
      if ~isa(obj.Parameters, 'Grasppe.PrintUniformity.Models.DataParameters')
        obj.Parameters = Grasppe.PrintUniformity.Models.DataParameters('Creator', obj);
      end
    end
    
    
    function caseID = get.CaseID(obj)
      caseID = '';
      try caseID = obj.Parameters.CaseID; end
    end
    
    
    %% SetID
    
    function set.SetID(obj, setID)
      
      % obj.initializeDataModels;
      
      parameters      = obj.Parameters;
      
      sheetID = 1;
      try sheetID     = obj.MetaProperties.SheetID.NativeMeta.DefaultValue; end
      try if ~isempty(parameters.SheetID),    sheetID     = parameters.SheetID; end; end
      
      variableID = 1;
      try variableID  = obj.MetaProperties.VariableID.NativeMeta.DefaultValue; end
      try if ~isempty(parameters.VariableID), variableID  = parameters.VariableID; end; end
      
      if ~isequal(obj.Parameters.SetID, setID)
        obj.Parameters.SetID    = setID;
        obj.Data.SetData        = [];
        
        obj.notify('SetChange');
      end
      
      obj.SheetID  = sheetID;
    end
    
    function setID = get.SetID(obj)
      setID = [];
      try setID = obj.Parameters.SetID; end
    end
    
    
    %% SheetID
    
    function set.SheetID(obj, sheetID)
      
      % obj.initializeDataModels;
      
      parameters      = obj.Parameters;
      
      variableID = 1;
      try variableID  = obj.MetaProperties.VariableID.NativeMeta.DefaultValue; end
      try if ~isempty(parameters.VariableID), variableID  = parameters.VariableID; end; end
      
      if ~isequal(obj.Parameters.SheetID, sheetID)
        obj.Parameters.SheetID  = sheetID;
        obj.notify('SheetChange');
      end
    end
    
    function sheetID = get.SheetID(obj)
      sheetID = [];
      try sheetID = obj.Parameters.SheetID; end
    end
    
    
    %% VariableID
    
    function set.VariableID(obj, variableID)
      if ~isequal(obj.Parameters.VariableID, variableID)
        obj.Parameters.VariableID  = variableID;
        obj.notify('VariableChange');
      end
    end
    
    function variableID = get.VariableID(obj)
      variableID = '';
      try variableID = obj.Parameters.VariableID; end
    end
    
    %% Case Data
    function set.CaseData(obj, caseData)
      data = obj.Data;
      [data.CaseData changed] = changeSet(data.CaseData, caseData);
      % if changed, obj.notify('CaseChange'); end
    end
    
    function caseData = get.CaseData(obj)
      caseData = [];
      
      %try caseData = obj.Data.CaseData; end
      if isempty(caseData), obj.getCaseData; end
      
      try caseData = obj.Data.CaseData; end
    end
    
    
    %% Set Data
    function set.SetData(obj, setData)
      data = obj.Data;
      [data.SetData changed] = changeSet(data.SetData, setData);
      % if changed, obj.notify('SetChange'); end
    end
    
    function setData = get.SetData(obj)
      setData = [];
      
      % try setData = obj.Data.SetData; end
      if isempty(setData), obj.getSetData; end
      
      try setData = obj.Data.SetData; end
    end
    
    
    %% Sheet Data
    function set.SheetData(obj, sheetData)
      data = obj.Data;
      [data.SheetData changed] = changeSet(data.SheetData, sheetData);
      % if changed, obj.notify('SheetChange'); end
    end
    
    function sheetData = get.SheetData(obj)
      sheetData = [];
      
      % try sheetData = obj.Data.SheetData; end
      if isempty(sheetData), obj.getSheetData; end
      
      try sheetData = obj.Data.SheetData; end
    end
    
    %% Data
    function data = get.Data(obj)
      % if isempty(obj.Data) obj.Data = UniformityProcessor; end
      data = obj.Data;
    end
    
    function set.Data(obj, data)
      if isempty(data) || isa(data, 'Grasppe.PrintUniformity.Models.UniformityData')
        obj.Data = data;
      end
    end
    
    function parameters = get.Parameters(obj)
      parameters = obj.Parameters;
    end
    
    function set.Parameters(obj, parameters)
      if isempty(parameters) || isa(parameters, 'Grasppe.PrintUniformity.Models.DataParameters')
        obj.Parameters = parameters;
      end
    end
    
    
    function getCaseData(obj)
      import Grasppe.PrintUniformity.Data.*;
      
      caseData  = [];   try caseData  = obj.Data.caseData;          end
      % caseID    = [];   try caseID    = obj.Parameters.CaseID;  end
      
      
      if isempty(obj.CaseID), return; end
      
      
      if isempty(caseData)
        obj.Data.SheetData  = [];
        obj.Data.SetData    = [];
        obj.Data.CaseData   = UniformityProcessor.processCaseData(obj.Parameters, obj.Data);
      end
      
      
    end
    
    function getSetData(obj)
      import Grasppe.PrintUniformity.Data.*;
      
      if isempty(obj.SetID), return; end
      
      obj.Data.SheetData  = [];
      obj.Data.SetData    = UniformityProcessor.processSetData(obj.Parameters, obj.Data);
      
    end
    
    function getSheetData(obj)
      import Grasppe.PrintUniformity.Data.*;
      
      if isempty(obj.SheetID), return; end
      
      obj.Data.SheetData  = UniformityProcessor.processSheetData(obj.Parameters, obj.Data);
      
    end
    
  end
  
  methods (Static)
    function caseData = processCaseData(parameters, data)
      import Grasppe.PrintUniformity.Data.*;
      
      caseData    = [];
      
      caseID      = parameters.CaseID;
      
      try if isequal(caseID, data.Parameters.CaseID)
          caseData = data.CaseData; end; end
      
      if isempty(caseData)
        data.Parameters.CaseID = [];
        
        try caseData  = Data.loadUPData(caseID); end
      end
      
      try data.CaseData 	= caseData;
        if ~isempty(caseData) data.Parameters.CaseID = caseID; end; end
      
    end
    
    function setData = processSetData(parameters, data)
      import Grasppe.PrintUniformity.Data.*;
      
      setData     = [];
      
      caseID      = parameters.CaseID;
      setID       = parameters.SetID;
      
      try if isequal(setID, data.Parameters.SetID)
          setData = data.SetData; end; end
      
      if isempty(setData)
        data.Parameters.SetID = [];
        
        caseData  = UniformityProcessor.processCaseData(parameters, data);
        
        setData   = struct( 'sourceName', caseID, 'patchSet', setID, ...
          'setLabel', ['tv' int2str(setID) 'data'], 'patchFilter', [], ...
          'data', [] );
        
        setData   = Data.filterUPDataSet(caseData, setData);
      end
      
      try data.SetData 	= setData;
        if ~isempty(setData) data.Parameters.SetID = setID; end; end
      
    end
    
    function sheetData = processSheetData(parameters, data)
      import Grasppe.PrintUniformity.Data.*;
      
      sheetData   = [];
      
      caseID      = parameters.CaseID;
      setID       = parameters.SetID;
      sheetID     = parameters.SheetID;
      variableID  = parameters.VariableID;
      
      try if isequal(sheetID, data.Parameters.SheetID)
          sheetData = data.SheetData; end; end
      
      if isempty(sheetData)
        data.Parameters.SheetID = [];
        
        setData           = UniformityProcessor.processSetData(parameters, data);
        
        try sheetData     = setData.data(sheetID).zData; end
      end
      
      try data.SheetData  = sheetData;
        if ~isempty(sheetData) data.Parameters.SheetID = sheetID; end; end
      
    end
  end
  
  methods(Static, Hidden)
    function options  = DefaultOptions()
      options = [];
    end
  end
  
end

