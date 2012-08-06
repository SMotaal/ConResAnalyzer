classdef UniformityPlaneDataSource < Grasppe.PrintUniformity.Data.UniformityDataSource
  %SURFACEUNIFORMITYDATASOURCE Raw printing uniformity data source
  %   Detailed explanation goes here
  
  properties
    % UniformityPlaneDataSourceProperties = {
    %   'TestProperty', 'Test Property', 'Labels', 'string', '';   ...
    %   };
    % TestProperty
  end
  
  methods (Hidden)
    function obj = UniformityPlaneDataSource(varargin)
      obj = obj@Grasppe.PrintUniformity.Data.UniformityDataSource(varargin{:});
    end
    
    function attachPlotObject(obj, plotObject)
      obj.attachPlotObject@Grasppe.PrintUniformity.Data.UniformityDataSource(plotObject);
      try plotObject.ParentAxes.ViewLock  = false; end
      try plotObject.ParentAxes.Box       = false; end
    end
    

    function [X Y Z] = processSheetData(obj, sheetID, variableID)

      [X Y Z]   = obj.processSheetData@Grasppe.PrintUniformity.Data.UniformityDataSource(sheetID, variableID);
      
      caseData      = obj.CaseData; ...
        setData   	= obj.SetData; ...
        sheetData   = obj.SheetData;
      
      targetFilter  = caseData.sampling.masks.Target~=1;
      patchFilter   = setData.filterData.dataFilter~=1;
      
      Z(~patchFilter)  = sheetData;
      Z(targetFilter) = NaN;
      Z(patchFilter)  = NaN;
      
      dataFilter  = ~isnan(Z);
      
      F = TriScatteredInterp(X(dataFilter), Y(dataFilter), Z(dataFilter), 'natural');
      
      Z = F(X, Y);
      Z(targetFilter) = NaN;

      
      % Z = Grasppe.PrintUniformity.Data.LocalVariabilityDataSource.localVariabilityFilter(Z);
      
      %Z(patchFilter~=1)   = NaN;      
      
    end
    
    function optimizeSetLimits(obj)
      
      xLim  = [];
      yLim  = [];
      zLim  = [];
      cLim  = [];
      
      obj.optimizeSetLimits@Grasppe.PrintUniformity.Data.UniformityDataSource(xLim, yLim, zLim, cLim);
      
    end    
    
  end
  
  methods (Static, Hidden)
%     function newData = rawUniformityFilter(zData)
%       newData = zData;
%       end
%     end

    function OPTIONS  = DefaultOptions()      
      Grasppe.Utilities.DeclareOptions;
    end
  end
  
  methods (Static)
    function obj = Create(varargin)
      obj = Grasppe.PrintUniformity.Data.UniformityPlaneDataSource(varargin{:});
    end
  end
  
  
end

