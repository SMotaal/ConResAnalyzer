classdef RegionStatsDataSource < Grasppe.PrintUniformity.Data.UniformityDataSource
  %REGIONSTATSDATASOURCE region-based printing uniformity statistics data source
  %   Detailed explanation goes here
  
  properties (AbortSet)
    % LocalVariabilityDataSourceProperties = {
    %   'TestProperty', 'Test Property', 'Labels', 'string', '';   ...
    %   };
    % TestProperty
    Regions
    Stats
    
    
    StatsFunction = [];
    DataFunction  = [];
    SummaryLength = 5;
    SummaryOffset = 1;
    
    PlotLabels    = [];
    PlotRegions   = [];
    PlotValues    = [];
  end
  
  properties (AbortSet)
    StatsMode
  end
  
  properties (SetAccess=private, GetAccess=private)
    CurrentStatsMode = 'Mean';
  end
  
  methods (Hidden)
    function obj = RegionStatsDataSource(varargin)
      obj = obj@Grasppe.PrintUniformity.Data.UniformityDataSource(varargin{:});
    end
    
    function attachPlotObject(obj, plotObject)
      obj.attachPlotObject@Grasppe.PrintUniformity.Data.UniformityDataSource(plotObject);
      try plotObject.ParentAxes.setView([0 90], true); end
      try plotObject.ParentAxes.Box       = true; end
      
      try delete(obj.PlotLabels); end
      try
        obj.PlotLabels = Grasppe.PrintUniformity.Graphics.UniformityPlotLabels;
        obj.PlotLabels.attachPlot(plotObject);
        obj.updatePlotLabels;
      catch err
        disp(err);
      end
      
    end
    
    function [X Y Z] = processSheetData(obj, sheetID, variableID)
      
      bufferSurfs = Grasppe.PrintUniformity.Options.Defaults.BufferSurfData';
      
      try
        
        [X Y Z]   = obj.processSheetData@Grasppe.PrintUniformity.Data.UniformityDataSource(sheetID, variableID);
        
        caseData      = obj.CaseData; ...
          setData   	= obj.SetData; ...
          sheetData   = obj.SheetData; ...
          stats       = obj.Stats; ...
          caseID      = obj.CaseID; ...
          setID       = obj.SetID; ...
          sheetID     = obj.SheetID; ...
          variableID  = obj.VariableID;
        
        % dispf('Sheet ID = %s', int2str(obj.SheetID));
        targetFilter  = caseData.sampling.masks.Target~=1;
        patchFilter   = setData.filterData.dataFilter~=1;
        
        
        if isempty(stats)
          Z(~patchFilter) = sheetData;
          Z(targetFilter) = NaN;
          Z(patchFilter)  = NaN;
          
          %[Z M] = Grasppe.PrintUniformity.Data.RegionStatsDataSource.regionStatisticsFilter(Z);
        else
          
          statsMode = regexprep(lower(obj.StatsMode), '\W', '');
          
          switch statsMode
            case {'std', 'deviation', 'standarddeviation'}
              statsMode     = 'Standard Deviation';
              statsFunction = @(d, r) vertcat(d.Std);
              dataFunction  = @(s)    nanstd(s(:));
              
              % statsData       = vertcat(regionData.Std);
              % try aroundData  = vertcat(aroundData.Std); end
              % try acrossData  = vertcat(acrossData.Std); end
            case {'deltalimits', 'deltalimit', 'sixsigma'}
              statsMode     = 'Six Sigma';
              statsFunction = @(d, r) vertcat(d.Std) .* 6;
              dataFunction  = @(s)    nanstd(s(:)) .* 6;
              
              % statsData = vertcat(regionData.Std) .* 6;
            case {'peaklimits', 'process limits'}
              statsMode     = 'Peak Limits';
              statsFunction = @(d, r) max(abs(vertcat(d.Lim) - r.Mean),[],2);
              dataFunction  = @(s)    nanmax(s(:));
              
              % limData   = vertcat(regionData.Lim);
              % statsData = max(abs(limData - runData.Mean),[],2);
            case {'lowerlimit'}
              statsMode     = 'Lower Limit';
              statsFunction = @(d, r) eval('vertcat(d.Lim); ans(:,1)');
              dataFunction  = @(s)    nanmin(s(:));
              
              
              % limData   = vertcat(regionData.Lim);
              % statsData = limData(:,1);
            case {'upperlimit'}
              statsMode     = 'Upper Limit';
              statsFunction = @(d, r) eval('vertcat(d.Lim); ans(:,2)');
              dataFunction  = @(s)    nanmax(s(:));
              % limData   = vertcat(regionData.Lim);
              % statsData = limData(:,2);
            otherwise % case {'mean', 'average'}
              statsMode     = 'Mean';
              statsFunction = @(d, r) vertcat(d.Mean);
              dataFunction  = @(s)    nanmean(s(:));
              % statsData = vertcat(regionData.Mean);
          end
          
          obj.CurrentStatsMode = statsMode;
          
          t = tic;
          
          statsID     = regexprep(lower(statsMode), '\W', '');
          sourceSpace =[caseID 'Surfs'];
          surfID      = Data.generateUPID(caseID,setID, [variableID int2str(sheetID) statsID]);
          surfData    = [];
          
          if bufferSurfs
            surfData    = Data.dataSources(surfID, sourceSpace);
          end
          
          if ~(isempty(surfData))
            try
              regionMasks = reshape(full(surfData.masks), surfData.size);
              regionStats = surfData.stats;
              newData     = surfData.data;
              % toc(t);
            catch err
              disp(err);
              surfData    = [];
            end
          end
          
          if (isempty(surfData))
            
            switch variableID
              case {'sections', 'around', 'across'}
                aroundID = 'around';
                acrossID = 'across';
              otherwise
                aroundID = [variableID 'Around'];
                acrossID = [variableID 'Across'];
            end
            
            regionMasks     = stats.metadata.regions.(variableID);
            regionData      = stats.(variableID)(:, sheetID);
            runData         = stats.run;
            
            try
              aroundMasks = stats.metadata.regions.(aroundID);
              aroundMasks = max(aroundMasks, [], 3);
              aroundData  = stats.(aroundID)(:, sheetID);
            catch
              aroundMasks = [];
              aroundData  = [];
            end
            
            try
              acrossMasks = stats.metadata.regions.(acrossID);
              acrossMasks = max(acrossMasks, [], 2);
              acrossData  = stats.(acrossID)(:, sheetID);
            catch
              acrossMasks = [];
              acrossData  = [];
            end
            
            regionStats = statsFunction(regionData, runData);
            
            rows      = size(Z,2);
            columns   = size(Z,1);
            
            summaryOffset = obj.SummaryOffset;
            offsetRange   = 1:summaryOffset;
            summaryRange  = summaryOffset + 1 + [0:obj.SummaryLength];
            summaryExtent = max(summaryRange);
            
            xColumns      = columns+summaryExtent;
            xColumnRange  = columns+1:xColumns;
            xRows         = rows+summaryExtent;
            xRowRange     = rows+1:xRows;
            
            regionMasks(:, xColumnRange, xRowRange) = false;
            
            newData       = zeros(1, xColumns, xRows);
            
            for m = 1:size(regionMasks,1)
              maskData          = regionMasks(m, :, :)==1;
              newData(maskData) = regionStats(m);
            end
            
            % obj.PlotRegions = regionMasks;
            % obj.PlotValues  = regionStats;
            
            try
              aroundStats = statsFunction(aroundData, runData);
              
              for m = 1:size(aroundMasks,1)
                xMask       = zeros(1, xColumns, xRows)==1;
                
                r = rows + summaryRange;
                aroundMask  = aroundMasks(m, :, :)==1;
                xMask(1, aroundMask(:), r) = true;
                
                
                newData(xMask) = aroundStats(m);
                
                n = size(regionMasks,1)+1;
                
                regionMasks(n, :, :)  = xMask;
                regionStats(n)        = aroundStats(m);
              end
              
            end
            
            try
              acrossStats = statsFunction(acrossData, runData);
              
              for m = 1:size(acrossMasks,1)
                xMask       = zeros(1, xColumns, xRows)==1;
                
                c = columns + summaryRange;
                acrossMask  = acrossMasks(m, :, :)==1;
                xMask(1, c, acrossMask(:)) = true;
                
                newData(xMask) = acrossStats(m);
                
                n = size(regionMasks,1)+1;
                
                regionMasks(n, :, :)  = xMask;
                regionStats(n)        = acrossStats(m);
              end
              
            end
            
            try
              sampleStats = dataFunction(regionStats);
              r = rows    + summaryRange;
              c = columns + summaryRange;
              newData(1, c, r) = sampleStats;
              
              xMask           = zeros(1, xColumns, xRows)==1;
              xMask(1, c, r)  = true;
              
              n = size(regionMasks,1)+1;
              
              regionMasks(n, :, :)  = xMask;
              regionStats(n)        = sampleStats;
              
            end
            
            if size(newData, 2) > columns,  newData(1, :, rows + offsetRange)     = nan; end
            if size(newData, 3) > rows,     newData(1, columns + offsetRange, :)  = nan; end
            
            try
              surfData.size   = size(regionMasks);
              surfData.masks  = sparse(squeeze(reshape(regionMasks,size(regionMasks,1),[])));
              surfData.stats  = regionStats;
              surfData.data   = newData;
            catch err
              disp(err);
            end
            
            if bufferSurfs
              Data.dataSources(surfID, surfData, true, sourceSpace);
            end
          end
          
          obj.PlotRegions = regionMasks;
          obj.PlotValues  = regionStats;
          
          Z = squeeze(newData);
          
          [X Y] = meshgrid(1:size(newData, 3), 1:size(newData, 2));
          
          %return;
        end
        
        % Z(targetFilter) = NaN;
        % Z(patchFilter)  = NaN;
        %
        %       dataFilter  = ~isnan(Z);
        %
        %       F = TriScatteredInterp(X(dataFilter), Y(dataFilter), Z(dataFilter), 'natural');
        %
        %       Z = F(X, Y);
        %       Z(targetFilter) = NaN;
        
        %Z(patchFilter~=1)   = NaN;
        
      catch err
        disp(err);
        % keyboard;
      end
      
      
    end
    
    function optimizeSetLimits(obj)
      
      zLength = 6;
      
      xLim  = [];
      yLim  = [];
      zLim  = [];
      cLim  = [];
      
      switch regexprep(lower(obj.StatsMode), '\W', '')
        case {'mean', 'average'}
          setData   = obj.SetData;
          
          zData     = [setData.data(:).zData];
          zMean     = nanmean(zData);
          
          zLim      = zMean + [+zLength/2 -zLength/2];
        case {'lowerlimit', 'upperlimit'}
          setData   = obj.SetData;
          
          zData     = [setData.data(:).zData];
          zMean     = nanmean(zData);
          zStd      = nanstd(zData,1);
          
          zLim      = zMean + [+zLength/2 -zLength/2];
        case {'std', 'deviation', 'standarddeviation'}
          zLim      = [0 10];
        case {'deltalimits', 'deltalimit', 'sixsigma'}
          zLim      = [0 10];
        case {'peaklimits', 'process limits'}
          zLim      = [0 10];
        otherwise
      end
      
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
  
  methods
    function updatePlotLabels(obj)
      try
        if ~isobject(obj.PlotLabels), return; end
        obj.PlotLabels.defineLabels(obj.PlotRegions, obj.PlotValues);
        obj.PlotLabels.createLabels;
      catch err
        disp(err);
        % try obj.PlotLabels.deleteLabels; end
      end
    end
    
    function updateCaseData(obj, source, event)
      [dataSource regions] = Metrics.generateUPRegions(obj.CaseData);
      obj.Regions = regions;
      
      obj.LastVariableID = '';
      % obj.updateVariableData(source, event);
      
      obj.updateCaseData@Grasppe.PrintUniformity.Data.UniformityDataSource(source, event);
      
      % try obj.PlotLabels.updateLabels; end;
      obj.updatePlotLabels;
    end
    
    function updateSetData(obj, source, event)
      obj.Stats = [];
      try obj.PlotLabels.deleteLabels; end
      obj.updateSetData@Grasppe.PrintUniformity.Data.UniformityDataSource(source, event);
      obj.updateVariableData(source, event);
      
      obj.updatePlotLabels;
    end
    
    function updateSheetData(obj, source, event)
      obj.updateSheetData@Grasppe.PrintUniformity.Data.UniformityDataSource(source, event);
      obj.updatePlotLabels;
    end
    
    function updateVariableData(obj, source, event)
      variableID = regexprep(source.VariableID, '\W', '');
      validID = false;
      if isstruct(obj.Regions)
        variableID = validatestring(variableID, fieldnames(obj.Regions));
        validID = true;
      end
      
      if ~validID
        switch lower(variableID)
          %           case {'raw', 'sections', 'around', 'across', 'zones', 'zonebands'}
          %             try
          %               if ~stropt(lower(source.VariableID), fieldnames(obj.Regions));
          %                 warning('Unknown variable ID, reverting to last variable ID.');
          %                 obj.VariableID = obj.LastVariableID; return;
          %               end
          %             catch err
          %               return;
          %             end
          case {'', [], 'none'}
            obj.VariableID = 'raw'; return;
          case {'sections', 'regions', 'region'}
            obj.VariableID = 'sections'; return;
          case {'around', 'circumferential'}
            obj.VariableID = 'around'; return;
          case {'across', 'axial'}
            obj.VariableID = 'across'; return;
          case {'zones', 'zone', 'inkzones'}
            obj.VariableID = 'zones'; return;
          case {'zoneband'}
            obj.VariableID = 'zoneBands'; return;
          otherwise
            try
              if ~isequal(obj.VariableID, obj.LastVariableID) && ~isempty(obj.LastVariableID)
                warning('Unknown variable ID, reverting to last variable ID.');
                obj.VariableID = obj.LastVariableID;
                return;
              end
            end
            %ids = fieldnames(obj.Regions);
            obj.VariableID = 'sections';
            return;
        end
      end
      
      if ~isequal(obj.VariableID, variableID)
        obj.VariableID = variableID;
      end
      
      if isequal(obj.VariableID, 'raw')
        obj.Stats = [];
      else
        switch obj.VariableID
          case {'sections', 'around', 'across'}
            regions.sections  = obj.Regions.sections;
            regions.around    = obj.Regions.around;
            regions.across    = obj.Regions.across;
          otherwise
            regions.(obj.VariableID)  = obj.Regions.(obj.VariableID);
            try regions.([obj.VariableID 'Around']) = obj.Regions.([obj.VariableID 'Around']); end
            try regions.([obj.VariableID 'Across']) = obj.Regions.([obj.VariableID 'Across']); end
        end
        
        [dataSource stats]    = Stats.generateUPStats(obj.CaseData, obj.SetData, regions);
        obj.Stats = stats;
      end
      
      % dispf('Variable ID = %s', obj.VariableID);
      
      try obj.PlotLabels.clearLabels; end
      
      obj.updateVariableData@Grasppe.PrintUniformity.Data.UniformityDataSource(source, event);
    end
    
    function set.StatsMode(obj, value)
      obj.StatsMode         = value;
      obj.CurrentStatsMode  = value;
      obj.updateSheetData([], []);
    end
    
    function value = get.StatsMode(obj)
      value = obj.CurrentStatsMode;
    end
    
  end
  
  methods (Static, Hidden)
    function stats = regionStatistics(caseData, setData)
      
    end
    
    function masks = regionMasks(caseData)
      %% Axial Masks
      
      %% Circumferential Masks
      
      %% Region Masks
      
    end
    
    function masks = zoneMasks(caseData)
      
    end
    
    function [newData metrics]  = regionStatisticsFilter(zData)
      %newData = zData;
      
      rows    = size(zData, 1);
      columns = size(zData, 2);
      
      if rows > columns
        across = 3;
        around = round(((rows/columns*3)-1)/2)*2+1;
      else % rows <= columns
        around = 3;
        across = round(((columns/rows*3)-1)/2)*2+1;
      end
      
      rowSteps          = reshape(([(rows)/around] * ceil(0:0.5:around-0.5)),2,[]);
      rowSteps          = round(rowSteps);
      rowSteps(1,:)     = rowSteps(1,:) + 1;
      
      columnSteps       = reshape(([(columns)/across] * ceil(0:0.5:across-0.5)),2,[]);
      columnSteps       = round(columnSteps);
      columnSteps(1,:)  = columnSteps(1,:) + 1;
      
      % try
      newData = zeros(size(zData));
      
      values  = zeros(around, across);
      
      for r = 1:around
        row = round(rowSteps(1,r):rowSteps(2,r));
        for c = 1:across
          column  = round(columnSteps(1,c):columnSteps(2,c));
          cellData              = zData(row, column);
          values(r,c)           = nanmean(cellData(:)); % ([(r-1)*across]+c)/(around*across)
          newData(row, column)  = values(r,c); % 1/(around*across)*(r*(c-1)+1);
        end
      end
      
      metrics.rowBands    = around;
      metrics.columnBands = across;
      metrics.rowSteps    = rowSteps;
      metrics.columnSteps = columnSteps;
      metrics.values      = values;
      % end
    end
    
    function OPTIONS  = DefaultOptions()
      VariableID = 'sections';
      
      Grasppe.Utilities.DeclareOptions;
    end
  end
  
  methods (Static)
    function obj = Create(varargin)
      obj = Grasppe.PrintUniformity.Data.LocalVariabilityDataSource(varargin{:});
    end
  end
  
  
end

