classdef LocalVariabilityDataSource < Grasppe.PrintUniformity.Data.UniformityDataSource
  %SURFACEUNIFORMITYDATASOURCE Raw printing uniformity data source
  %   Detailed explanation goes here
  
  properties
    % LocalVariabilityDataSourceProperties = {
    %   'TestProperty', 'Test Property', 'Labels', 'string', '';   ...
    %   };
    % TestProperty
  end
  
  methods (Hidden)
    function obj = LocalVariabilityDataSource(varargin)
      obj = obj@Grasppe.PrintUniformity.Data.UniformityDataSource(varargin{:});
    end
    
    function attachPlotObject(obj, plotObject)
      obj.attachPlotObject@Grasppe.PrintUniformity.Data.UniformityDataSource(plotObject);
      try plotObject.ParentAxes.setView([0 90], true); end
      try plotObject.ParentAxes.Box       = true; end
    end

    function [X Y Z] = processSheetData(obj, sheetID, variableID)

      [X Y Z]   = obj.processSheetData@Grasppe.PrintUniformity.Data.UniformityDataSource(sheetID, variableID);
      
      caseData      = obj.CaseData; ...
        setData   	= obj.SetData; ...
        sheetData   = obj.SheetData;
      
      targetFilter  = caseData.sampling.masks.Target~=1;
      patchFilter   = setData.filterData.dataFilter~=1;
      
      Z(~patchFilter) = sheetData;
      Z(targetFilter) = NaN;
      Z(patchFilter)  = NaN;
      
      Z = Grasppe.PrintUniformity.Data.LocalVariabilityDataSource.localVariabilityFilter(Z);
      
      Z(targetFilter) = NaN;
      Z(patchFilter)  = NaN;
      
      dataFilter  = ~isnan(Z);
      
      F = TriScatteredInterp(X(dataFilter), Y(dataFilter), Z(dataFilter), 'natural');
      
      Z = F(X, Y);
      Z(targetFilter) = NaN;
      
      %Z(patchFilter~=1)   = NaN;      
      
    end
    
    function optimizeSetLimits(obj)
      
      xLim  = [];
      yLim  = [];
      zLim  = [0 10];
      cLim  = [];
      
      obj.optimizeSetLimits@Grasppe.PrintUniformity.Data.UniformityDataSource(xLim, yLim, zLim, cLim);
      
    end
    
    function setPlotData(obj, XData, YData, ZData)
      try debugStamp(obj.ID); catch, debugStamp(); end;
      obj.XData = XData;
      obj.YData = YData;
      %obj.ZData = ZData;
      obj.CData = ZData;
      
      %ZData(~isnan(ZData)) = nanmean(ZData(:));
      
      obj.ZData = ZData;
      
      obj.updatePlots();
    end
    
  end
  
  methods (Static, Hidden)
    function newData = localVariabilityFilter(zData)
      newData = [];
      
      try
        y = 1; yNan = [];
        while isempty(yNan)       
          yNan  = find(~isnan(zData(:,y)));
          y     = y +1;
        end
        yStep   = abs(mode(yNan - yNan([2:end 1])));
        
        x = 1; xNan = [];
        while isempty(xNan)       
          xNan  = find(~isnan(zData(x,:)));
          x     = x +1;
        end        
        
        xStep   = abs(mode(xNan - xNan([2:end 1])));
        
        ySub    = yStep+1:yStep+size(zData,1);
        xSub    = xStep+1:xStep+size(zData,2);
        
        pSize   = size(zData) + [yStep*2 xStep*2];
        
        pData             = ones(pSize) * nan;
        pData(ySub, xSub) = zData;
        
        dData   = zeros(size(pData));
        
        for xi = [-xStep 0 +xStep]
          for yi = [-yStep 0 +yStep]
            fData                 = abs(pData-circshift(pData,[yi xi]));
            fData(isnan(fData))   = 0 ;
            dData                 = dData + fData;
          end
        end
        
        newData   = dData(ySub, xSub);
      end
    end
    function OPTIONS  = DefaultOptions()      
      Grasppe.Utilities.DeclareOptions;
    end
  end
  
  methods (Static)
    function obj = Create(varargin)
      obj = Grasppe.PrintUniformity.Data.LocalVariabilityDataSource(varargin{:});
    end
  end
  
  
end

