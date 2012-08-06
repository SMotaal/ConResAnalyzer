classdef MultiPlotFigure < Grasppe.Graphics.PlotFigure
  %MULTIPLOTFIGURE Summary of this class goes here
  %   Detailed explanation goes here
  
  properties
    PlotAxesTargets = []; % = struct('id', [], 'idx', [], 'object', []);
    PlotAxesStack   = [];
    PlotAxesLength  = 1;
    
    PlotRows;
    PlotColumns;
    PlotWidth;
    PlotHeight;
    PlotArea;
    HiddenFigure    = figure('Visible', 'off');
    
  end
  
  methods
    
    function obj = MultiPlotFigure(varargin)
      obj = obj@Grasppe.Graphics.PlotFigure(varargin{:});
    end
    
    
    function OnResize(obj, source, event)
      obj.OnResize@Grasppe.Graphics.PlotFigure;
      obj.layoutPlotAxes;
      obj.layoutOverlay;
      
    end
  end
  
  methods (Access=protected)
    function createComponent(obj)
      obj.createComponent@Grasppe.Graphics.PlotFigure();
    end
    
    
    function preparePlotAxes(obj)
      try debugStamp(obj.ID); catch, debugStamp(); end;
      nPlots      = obj.PlotAxesLength;
      plotStack   = obj.PlotAxesStack;
      plotTargets = obj.PlotAxesTargets;
      
      if ~iscell(obj.PlotAxes) || ~isstruct(obj.PlotAxesTargets) || ~isnumeric(obj.PlotAxesStack) %|| any(cellfun('isempty',obj.PlotAxes))
        obj.PlotAxesStack   = 1:nPlots;
        obj.PlotAxesTargets = repmat(struct('id', [], 'object', []),1, nPlots);
        obj.PlotAxes        = cell(size(obj.PlotAxesTargets));
        
        for i = 1:nPlots
          obj.createPlotAxes([], []);
        end
      else
        disp('Flagged code in MutliPlotFigure is being used!');
        if ~isnumeric(plotStack) || length(plotStack)~=nPlots
          plotStack   = 1:nPlots;
          try
            newStacks = setdiff(plotStack, obj.PlotAxesStack);
            plotStack = [newStacks, obj.PlotAxesStack];
          end
        end
        
        if ~isstruct(plotTargets) || length(plotTargets)~=nPlots
          plotTargets(1:nPlots) = struct('id', [], 'object', []);
          try
            currentLength = length(obj.PlotAxesTargets);
            plotTargets(1:currentLength) = obj.PlotAxesTargets(:);
          end
        end
        
        obj.PlotAxesTargets = plotTargets;
        obj.PlotAxesStack   = plotStack;
        obj.PlotAxes        = plotTargets.object;
        
      end
      
      
    end
    
    function [plotAxes idx id] = createPlotAxes(obj, idx, id)
      try debugStamp(obj.ID); catch, debugStamp(); end;
      nextIdx = idx;  nextID  = id;
      
      nAxes = length(obj.PlotAxes);
      
      emptyIdx  = cellfun('isempty', {obj.PlotAxesTargets.object});
      
      if ~isempty(nextID) && ischar(nextID)
        targetIdx = strcmpi(nextID, {obj.PlotAxesTargets.id});
        if any(targetIdx)
          nextIdx = find(targetIdx,1,'first');
        else
          nextIdx = [];
        end
      end
      
      if isempty(nextIdx) || ~isnumeric(nextIdx) || nextIdx > nAxes || nextIdx < 1
        if any(emptyIdx);
          nextIdx   = find(emptyIdx,1,'first');
        else
          obj.PlotAxesStack = obj.PlotAxesStack([2:end 1]);
          nextIdx   = obj.PlotAxesStack(end);
        end
      end
      
      if isempty(nextID) || ~ischar(nextID)
        nextID = '';
      end
      
      plotAxesStruct = obj.PlotAxesTargets(nextIdx);
      
      plotAxesStruct.id = nextID;
      
      try
        plotAxesStruct.object.clearAxes()
        if ~plotAxesStruct.object.isvalid
          plotAxesStruct.object = PlotAxesObject.Create(obj);
        end
      catch
        plotAxesStruct.object = Grasppe.Graphics.PlotAxes('ParentFigure', obj);
      end
      
      obj.PlotAxesTargets(nextIdx)  = plotAxesStruct;
      obj.PlotAxes(nextIdx)         = {plotAxesStruct.object};
      
      plotAxes  = plotAxesStruct.object;
      idx       = nextIdx;
      id        = nextID;
      
      obj.layoutPlotAxes();
    end
    
    function layoutOverlay(obj)
      
      plotArea      = obj.PlotArea;
      
      try
        obj.TitleText.handleSet('Units', 'pixels');
        
        titlePosition = obj.TitleText.handleGet('Position');
        titlePosition = [plotArea(1) plotArea(2)+plotArea(4) titlePosition(3)];
        
        plotInset     = obj.PlotAxes{1}.handleGet('TightInset');
        
        titlePosition = titlePosition + [0 plotInset(2)+plotInset(4) 0];
      end
      
      try obj.TitleText.handleSet('Position', titlePosition); end
      
    end    
    
    
    function layoutPlotAxes(obj)
      try
        cells           = sum(~cellfun(@isempty,obj.PlotAxes));
        parentPosition  = pixelPosition(obj.Handle);
        margins         = [20 20 20 20]; % L/B/R/T
        spacing         = 60; %-50;
        padding         = [30 30 30 10];
        minimumSize     = [150 125]; %W/H
        sizingRatio     = 1.25;
        
        plottingWidth   = parentPosition(3) - margins(1) - margins(3);
        plottingHeight  = parentPosition(4) - margins(2) - margins(4);
        % fittingWidth    = plottingWidth;
        % fittingHeight   = plottingHeight;
        
        minCellWidth    = minimumSize(1);
        minCellHeight   = minimumSize(2);
        cellWidthRatio  = minCellWidth / minCellHeight;
        
        % Determine maximum columns fit along width
        maxColumns    = floor(plottingWidth/minCellWidth);
        % minRows       = ceil(cells/maxColumns);
        
        % Detemine maximum rows fit along height
        maxRows       = floor(plottingHeight/minCellHeight);
        minColumns    = ceil(cells/maxRows);
        
        % Determine maximum area fit by rows & columns
        columns = 0; rows = 0; maxU = 0;
        
        for w = minCellWidth:plottingWidth
          h  = w/cellWidthRatio;
          for c = minColumns:maxColumns
            r = ceil(cells/c);
            wT = c*w;
            hT = r*h;
            u = (w*h*cells)/(plottingWidth*plottingHeight);
            if (u>maxU) && (u<=1) && wT<=plottingWidth && hT<=plottingHeight
              columns     = c;
              rows        = r;
              cellWidth   = floor(w);
              cellHeight  = floor(h);
              maxU        = u;
            end
          end
        end
        
        if maxU==0
          try
            cellWidth   = obj.PlotWidth;
            cellHeight  = obj.PlotHeight;
            columns     = obj.PlotColumns;
            rows        = obj.PlotRows;
          catch
            cellWidth   = minCellWidth;
            cellHeight  = minCellHeight;
            columns     = round(cells/2);
            rows        = ceil(cells / columns);
          end
        end
        
        obj.PlotWidth   = cellWidth;
        obj.PlotHeight  = cellHeight;
        obj.PlotColumns = columns;
        obj.PlotRows    = rows;
        
        fittingWidth    = cellWidth*columns;
        fittingHeight   = cellHeight*rows;
        
        fittingLeft     = margins(1) + (plottingWidth-fittingWidth) / 2;
        fittingBottom   = max(0, margins(2) + (plottingHeight-fittingHeight));
        
        plotWidth       = cellWidth   - spacing;
        plotHeight      = cellHeight  - spacing;
        
        if plotWidth>plotHeight*sizingRatio
          plotWidth = plotHeight*sizingRatio;
        elseif plotHeight>plotWidth/sizingRatio;
          plotHeight = plotWidth/sizingRatio;
        end
        
        cellWidth       = max(cellWidth, plotWidth);
        cellHeight      = max(cellHeight, plotHeight);

        lastOffset      = cellWidth/2*(columns - (cells - ((rows-1)*columns)));
        
        plotSize        = [plotWidth-padding(1)-padding(3) plotHeight-padding(2)-padding(4)];
        
        cellLeft        = (cellWidth - plotWidth) / 2;
        cellBottom      = (cellHeight - plotHeight) / 2;
        
        plotPosition    = zeros(cells, 4);
        
        for m = 1:cells
          [column row]  = ind2sub([columns rows], m);
          if (row == rows)
            plotLeft      = fittingLeft   + lastOffset + cellLeft   + padding(1) + (cellWidth  * (column - 1));
            plotBottom    = fittingBottom + cellBottom + padding(2) + (cellHeight * (rows-row));
          else
            plotLeft      = fittingLeft   + cellLeft   + padding(1) + (cellWidth  * (column - 1));
            plotBottom    = fittingBottom + cellBottom + padding(2) + (cellHeight * (rows-row));
          end
          
          plotPosition(m, :) = round([plotLeft plotBottom plotSize]);

          try
            if plotBottom < (plottingHeight)
              obj.PlotAxes{m}.handleSet('Parent', obj.Handle);
              if ~isempty(obj.PlotAxes{m}) && ishandle(obj.PlotAxes{m}.Handle)
                obj.PlotAxes{m}.handleSet('ActivePositionProperty', 'OuterPosition');
                obj.PlotAxes{m}.handleSet('Units', 'pixels');
                obj.PlotAxes{m}.handleSet('Position', plotPosition(m, :));
              end
            else
              obj.PlotAxes{m}.handleSet('Parent', obj.HiddenFigure);
            end
          catch err
            try debugStamp(obj.ID); end
            dispf('Layout FAILED for %s', obj.PlotAxes{m}.ID);
          end
        end
        
        areaMin = min(plotPosition, [], 1);
        areaMax = max(plotPosition, [], 1);        
        areaPosition = [areaMin(1:2)  areaMin(1:2)-areaMax(1:2)+areaMax(3:4)];
        
        obj.PlotArea = areaPosition;
        
      catch err
        try debugStamp(obj.ID); end
        %disp(err);
      end
    end
    
    
    
  end
  
end

