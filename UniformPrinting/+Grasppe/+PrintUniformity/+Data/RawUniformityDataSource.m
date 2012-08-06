classdef RawUniformityDataSource < Grasppe.PrintUniformity.Data.UniformityDataSource
  %SURFACEUNIFORMITYDATASOURCE Raw printing uniformity data source
  %   Detailed explanation goes here
  
  properties
    % RawUniformityDataSourceProperties = {
    %   'TestProperty', 'Test Property', 'Labels', 'string', '';   ...
    %   };
    % TestProperty
  end
  
  methods (Hidden)
    function obj = RawUniformityDataSource(varargin)
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
      
      dataFilter    = ~(targetFilter | patchFilter);
      
      Z(~patchFilter)  = sheetData;
      
      F = TriScatteredInterp(X(dataFilter), Y(dataFilter), Z(dataFilter));
      
      XRange = 0.25 + [1:obj.getColumnCount*2+0.25]./2;
      YRange = 0.25 + [1:obj.getRowCount*2+0.25]./2;
      
      [X2 Y2] = meshgrid(XRange, YRange);
      
      D2 = im2bw(interp2(X,Y,double(dataFilter),X2,Y2));
      
      Z2 = F(X2, Y2);
      Z2(~D2) = nan;
      
      X = X2; Y = Y2; Z = Z2;
%       Z(targetFilter) = NaN;
%       
%       X(dataFilter) = NaN;
%       Y(dataFilter) = NaN;
%       Z(targetFilter) = NaN;
%       Z(patchFilter)  = NaN;
%       
%       dataFilter  = ~isnan(Z);
%       
%       X(~dataFilter) = NaN;
%       Y(~dataFilter) = NaN;
      
%       F = TriScatteredInterp(X(dataFilter), Y(dataFilter), Z(dataFilter));
%       
%       Z = F(X, Y);
%       Z(targetFilter) = NaN;

      
      % Z = Grasppe.PrintUniformity.Data.LocalVariabilityDataSource.localVariabilityFilter(Z);
      
      %Z(patchFilter~=1)   = NaN;      
      
    end
    
%     function optimizeSetLimits(obj)
%       % zLim    = [0 10];
%       
%       obj.ZLim  = 'auto';
%       % obj.CLim  = 'auto';
%     end
    
    
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
      obj = Grasppe.PrintUniformity.Data.RawUniformityDataSource(varargin{:});
    end
  end
  
  
end

