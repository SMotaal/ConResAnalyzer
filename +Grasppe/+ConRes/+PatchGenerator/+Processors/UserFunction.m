classdef UserFunction < Grasppe.ConRes.PatchGenerator.Processors.ImageProcessor
  %PATCHGENERATOR Summary of this class goes here
  %   Detailed explanation goes here
  
  properties
  end
  
  methods
    function output = Run(obj)
      output  = obj.Input;
      params  = obj.Parameters;

      try      
        obj.sandbox(params, output);
      catch err
        disp('Failed to execute function');
        disp(err);
      end
      

    end
    
    function sandbox(obj, params, output)
      fourier   = @(method,mode) obj.fourier(method, mode);
      
      processData = output.ProcessData;
      %resolution = @
      
      % fx = varargin{1};
      
      structVars(params);
      
      image = output.Image;
      
      image = eval(findField(params, 'expression'));
        
      output.Image = image;
      
      return;      
    end
    
    function image = fourier(obj, method, mode)
      output  = evalin('caller', 'output');
      image   = evalin('caller', 'image');
      
      %method  = 1;
      
      switch (output.Domain)
        case 'frequency'
        % case 'spatial'  
          if method == 0
            image = 1-exp(image);
          end
          image = ifft2(ifftshift(image));
          output.Domain = 'spatial';
        otherwise
          image = fftshift(fft2(image));
          if method == 0
            image = log(1+abs(image));
            image = (image-min(image(:)));
            image = image / max(image(:));
          end
          output.Domain = 'frequency';
      end
      
      disp([method mode]);
    end
  end
end
