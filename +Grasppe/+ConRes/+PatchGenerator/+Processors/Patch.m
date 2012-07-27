classdef Patch < Grasppe.ConRes.PatchGenerator.Processors.ImageProcessor
  %PATCHGENERATOR Summary of this class goes here
  %   Detailed explanation goes here
  
  properties
    Addressability = 3600;
    Scale          = 1.0;
  end
  
  methods
    
    function output = Run(obj)
      output          = obj.Input;
      variables       = obj.Variables;
      
      parameters      = obj.Parameters;
      
      addressibility  = obj.Addressability;
      
      reference       = findField(parameters, 'mean');
      contrast        = findField(parameters, 'contrast');
      resolution      = findField(parameters, 'resolution');
      size            = findField(parameters, 'size');
      
      [image spec] = Grasppe.Kit.ConRes.GeneratePatchImage(reference, contrast, resolution, size, obj.Addressability*obj.Scale); %, width, addressability);
      
      if obj.Scale~=1.0
        image= imresize(image,1/obj.Scale);
      end
      
      parameters.(CONRES.Mean')       = spec(1);
      parameters.(CONRES.Contrast')   = spec(2);
      parameters.(CONRES.Resolution') = spec(3);
      parameters.(CONRES.Size')       = size;
      
      output.setImage(im2double(image), obj.Addressability);
      
      variables.Patch = parameters;
      obj.Variables   = variables;
    end
    
  end
  
end

