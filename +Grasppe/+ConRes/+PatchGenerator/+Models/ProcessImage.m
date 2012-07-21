classdef ProcessImage < handle
  %IMAGEPARAMETERS Summary of this class goes here
  %   Detailed explanation goes here
  
  properties (Dependent)
    Width
    Height
    Depth
    Resolution
    Unit
    Mode
    Process
    ProcessData
    Image
    Domain
  end
  
  properties (Access=private)
    width
    height
    depth
    resolution  = 2400;
    unit        = 'Inch';
    mode
    process     = {};
    processData = Grasppe.Occam.ProcessData.empty;
    image
    domain = 'spatial';
  end
  
  methods
    function width = get.Width(obj)
      width = obj.width;
    end
    
    function set.Width(obj, width)
      if ~isequal(obj.width, width)
        obj.width = width;
      end
    end
    
    function height = get.Height(obj)
      height = obj.height;
    end
    
    function set.Height(obj, height)
      if ~isequal(obj.height, height)
        obj.height = height;
      end
    end
    
    function depth = get.Depth(obj)
      depth = obj.depth;
    end
    
    function set.Depth(obj, depth)
      if ~isequal(obj.depth, depth)
        obj.depth = depth;
      end
    end
    
    function resolution = get.Resolution(obj)
      resolution = obj.resolution;
    end
    
    function set.Resolution(obj, resolution)
      if ~isequal(obj.resolution, resolution)
        obj.resolution = resolution;
      end
    end
    
    function unit = get.Unit(obj)
      unit = obj.unit;
    end
    
    function set.Unit(obj, unit)
      if ~isequal(obj.unit, unit)
        obj.unit = unit;
      end
    end
    
    function mode = get.Mode(obj)
      mode = obj.mode;
    end
    
    function set.Mode(obj, mode)
      if ~isequal(obj.mode, mode)
        obj.mode = mode;
      end
    end
    
    function process = get.Process(obj)
      process = obj.process;
    end
    
%     function set.Process(obj, process)
%       if ~isequal(obj.process, process)
%         obj.process = process;
%       end
%     end

    function setImage(obj, image, resolution)
      obj.Image       = image;
      obj.Resolution  = resolution;
        
    end

    function processData = get.ProcessData(obj)
      processData = obj.processData;
    end

    function addProcess(obj, process)
      try
        processData = process.getProcessData;
        obj.process{end+1}      = process;
        obj.processData(end+1)  = processData;
      end
    end
    
    function image = get.Image(obj)
      image = obj.image;
    end
    
    function set.Image(obj, image)
      if ~isequal(obj.image, image)
        obj.image = image;
        obj.updateMetadata();
      end
    end
    
    function updateMetadata(obj)
      image   = obj.image;
      
      width   = size(image, 2);
      height  = size(image, 2);
      depth   = ByteSize(image(1,1,1));
      
      mode    = obj.mode;
      
      switch size(image,3)
        case 1
          if depth < 1
            mode  = 'grayscale';
          else
            mode  = 'bitmap';
          end
        case 2
          mode = 'duotone';
        case 3
          if isempty(mode) || isequal(mode, 'rgb')
            mode = 'rgb';
          end
        case 4
          mode = 'cmyk';
        otherwise
          mode = 'multichannel';
      end
            
      obj.width   = width;
      obj.height  = height;
      obj.depth   = depth;
      obj.mode    = mode;
      
    end
    
    
    function domain = get.Domain(obj)
      domain = obj.domain;
    end
    
    function set.Domain(obj, domain)
      if ~isequal(obj.domain, domain)
        obj.domain = domain;
      end
    end
    
    
    
  end
  
end

