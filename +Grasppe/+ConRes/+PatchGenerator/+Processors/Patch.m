classdef Patch < Grasppe.ConRes.PatchGenerator.Processors.ImageProcessor
  %PATCHGENERATOR Summary of this class goes here
  %   Detailed explanation goes here
  
  properties
    Addressability = 3600;
    Scale          = 1.0;
  end
  
  properties (Constant)
    MEANTONE    = Grasppe.ConRes.Enumerations.Mean';
    CONTRAST    = Grasppe.ConRes.Enumerations.Contrast';
    RESOLUTION  = Grasppe.ConRes.Enumerations.Resolution';
    SIZE        = Grasppe.ConRes.Enumerations.Size';
  end
  
  methods
    
    function output = Run(obj)
      
      import(eval(NS.CLASS));
      
      output          = obj.Input;
      variables       = obj.Variables;
      
      parameters      = obj.Parameters;
      
      addressibility  = obj.Addressability;
      
      meantone        = findField(parameters, 'mean');
      contrast        = findField(parameters, 'contrast');
      resolution      = findField(parameters, 'resolution');
      size            = findField(parameters, 'size');
      
      [image spec] = Grasppe.Kit.ConRes.GeneratePatchImage(meantone, contrast, resolution, size, obj.Addressability*obj.Scale); %, width, addressability);
      
      if obj.Scale~=1.0
        image= imresize(image,1/obj.Scale);
      end
      
      parameters.(Patch.MEANTONE)   = spec(1);
      parameters.(Patch.CONTRAST)   = spec(2);
      parameters.(Patch.RESOLUTION) = spec(3);
      parameters.(Patch.SIZE)       = size;
      
      output.setImage(im2double(image), obj.Addressability);
      
      variables.Patch = parameters;
      obj.Variables   = variables;
    end
    
  end  
  
end

