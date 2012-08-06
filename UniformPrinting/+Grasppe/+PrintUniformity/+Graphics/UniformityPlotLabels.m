classdef UniformityPlotLabels < Grasppe.Core.Component
  %UNIFORMITYPLOTLABELS Summary of this class goes here
  %   Detailed explanation goes here
  
  properties
    LabelObjects    = {};
    LabelValues     = [];
    LabelRegions    = [];
    LabelPositions  = [];
    LabelActivePositions  = [];
    LabelAreas      = [];
    LabelElevation  = 100;
    PlotObject
    ComponentType   = '';
    FontSize        = 6;
  end
  
  methods
    
    function obj = UniformityPlotLabels()
      obj = obj@Grasppe.Core.Component();
    end
    
    function set.PlotObject(obj, plotObject)
      try obj.deleteLabels; end
      
      obj.PlotObject = plotObject;
    end
    
    function attachPlot(obj, plotObject)
      
      obj.PlotObject = plotObject;
      
      %% Elevation
      zReverse = false;
      try zReverse = isequal(lower(plotObject.ZDir), 'reverse'); end
      
      z = 100;
      
      try
        if zReverse
          z = min(plotObject.HandleObject.ZLim) - 3;
        else
          z = max(plotObject.HandleObject.ZLim) + 3;
        end
      end
      
      obj.LabelElevation = z;
    end
    
    function clearLabels(obj)
      for m = 1:numel(obj.LabelObjects)
        obj.LabelObjects{m}.Text = '';
      end
    end
    
    function deleteLabels(obj)
      try
        for m = 1:numel(obj.LabelObjects)
          try delete(obj.LabelObjects{m}); end
          obj.LabelObjects{m} = [];
        end
      end
      try
        for m = 1:numel(obj.LabelObjects)
          try if isempty(obj.LabelObjects{m}), continue; end; end
          return;
        end
      end
      try
        obj.LabelObjects    = {};
        obj.LabelValues     = [];
        obj.LabelRegions    = [];
        obj.LabelPositions  = [];
      end
    end
    
    function defineLabels(obj, regions, values)
      %obj.deleteLabels;
      obj.LabelRegions    = regions;
      obj.LabelValues     = values;
      obj.LabelPositions  = [];
    end
    
    function createLabels(obj)
      try
        values  = obj.LabelValues;
        regions = obj.LabelRegions;
        
        
        for m = 1:numel(obj.LabelValues)
          try
            region = eval(['regions(m' repmat(',:',1,ndims(regions)-1)  ')']);
            obj.createLabel(m, squeeze(region), values(m));
          end
        end
      catch err
        disp(err);
      end
      
    end
    
    
    function createLabel(obj, index, region, value)
      try
        if ~isa(obj.PlotObject, 'Grasppe.Graphics.PlotComponent') || ...
            ~isa(obj.PlotObject.ParentAxes, 'Grasppe.Graphics.PlotAxes')
          return;
        end
      catch
        return;
      end
      
      try
        
        %% Index
        if isempty(index)
          index = numel(obj.LabelObjects)+1;
        else
          label = [];
          try label = obj.LabelObjects{index}; end
        end
        
        %% Label
        if isempty(label) % Create Label
          try
            label = Grasppe.Graphics.TextObject(obj.PlotObject.ParentAxes, 'Text', int2str(index));
            label.HandleObject.HorizontalAlignment  = 'center';
            label.HandleObject.VerticalAlignment    = 'middle';
            % label.FontSize    = 5;
            label.IsClickable = false;
          catch err
            warning('Plot must be attached before creating labels');
            return;
          end
          obj.registerHandle(label);
          obj.LabelObjects{index} = label;
        end
        
        try label.FontSize = obj.FontSize; end
        
        %% Region (xmin ymin width height)
        if isequal(size(region), [1 4])
          % no change
        elseif isequal(size(region), [1 2])
          region  = [region 0 0];
        else % is a mask
          y       = nanmax(region, [], 2);
          y1      = find(y>0, 1, 'first');
          y2      = find(y>0, 1, 'last');
          
          x       = nanmax(region, [], 1);
          x1      = find(x>0, 1, 'first');
          x2      = find(x>0, 1, 'last');
          
          region  = [x1 y1 x2-x1 y2-y1];
        end
        
        dimension = region([3 4]);
        
        %% Position (centering)
        position  = region([1 2]) + dimension/2;
        
        
        obj.LabelRegions(index, 1:4)    = region;
        obj.LabelPositions(index, 1:2)  = position;
        obj.LabelAreas(index, 1:2)      = dimension;
        
        %% Value
        if nargin < 3, value = []; end
        
        try if isempty(value), value = obj.LabelValues(index); end; end
        
        obj.LabelValues(index) = value;
        
        obj.updateLabel(index);
        
      catch err
        disp(err);
      end
      
      
    end
    
    function updateLabel(obj, index)
      try
        value = [];
        
        try value = obj.LabelValues(index); end
        
        if isa(value, 'double'), value = num2str(value, '%3.1f');
          %else, value = '';
        end
        
        try obj.LabelObjects{index}.Text = toString(value); end
        
        position = [-100 -100];
        try 
          position = obj.LabelPositions(index, :); 
          
          try
            extent = obj.LabelObjects{index}.HandleObject.Extent;
            region = obj.LabelAreas(index, :);
            
            if extent(3)*0.8 > region(1)
              position(2) = position(2) + (rem(index,2)*2-1)*1.5;
            end
            
            if extent(4)*0.8 > region(2)
              position(1) = position(1) + (rem(index,2)*2-1)*1.5;
            end
          end
        end
        try obj.LabelObjects{index}.Position = [position obj.LabelElevation]; end
      catch err
        disp(err);
      end
    end
    
    function updateLabels(obj)
      for m = 1:numel(obj.LabelObjects)
        obj.updateLabel(m);
      end
    end
  end
  
  methods (Access=protected)
    %     function createComponent(obj)
    %
    %       try
    %         componentType = obj.ComponentType;
    %       catch err
    %         error('Grasppe:Component:MissingType', ...
    %           'Unable to determine the component type to create the component.');
    %       end
    %
    %       obj.intializeComponentOptions;
    %
    %       obj.Initialized = true;
    %
    %     end
  end
  
  methods (Static)
    
    function OPTIONS  = DefaultOptions()
      Grasppe.Utilities.DeclareOptions;
    end
  end
  
  
end

