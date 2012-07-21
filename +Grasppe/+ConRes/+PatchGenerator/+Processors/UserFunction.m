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
      fourier   = @(varargin) obj.fourier(varargin{:});
      
      processData = output.ProcessData;
      %resolution = @
      
      % fx = varargin{1};
      
      structVars(params);
      
      image = output.Image;
      
      image = eval(findField(params, 'expression'));
        
      output.Image = image;
      
      return;      
    end
    
    function image = fourier(obj, varargin)
      output  = evalin('caller', 'output');
      image   = evalin('caller', 'image');
      
      method  = 1;
      
      
      switch (output.Domain)
        case 'frequency'
          
          % Unpadding
          sP  = size(image,1);
          sQ  = size(image,2);          
          fP  = ceil(sP/2);
          fQ  = ceil(sQ/2);
          image = ifft2(ifftshift(image)); %, fP, fQ);
          image = image(1:fP, 1:fQ);
          output.Domain = 'spatial';

        otherwise % case 'spatial' 

          % Sizing & Padding
          sP  = size(image,1);
          sQ  = size(image,2);
          nP  = 1-mod(sP,2);
          nQ  = 1-mod(sQ,2);
          fP  = ceil(2*(sP-nP));
          fQ  = ceil(2*(sQ-nQ));
          
          image = image(1:end-(nP),1:end-(nQ));

          image = fftshift(fft2(image, fP, fQ));
          output.Domain = 'frequency';
      end
      
      disp([method varargin{1}]);
    end
  end
end
