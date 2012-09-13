classdef ImageProcessor < Grasppe.Occam.Process
  %PATCHGENERATOR Summary of this class goes here
  %   Detailed explanation goes here
  
  properties (Transient)
    View
    Controller
  end
  
  methods
    
    %     function obj = ImageProcessor(view)
    %       obj = obj@Grasppe.Occam.Process();
    %       obj.View = view;
    %     end
    
    
    function input = InitializeProcess(obj, input)
      try
        if isnumeric(input) && ndims(input) <= 3;
          input = obj.createProcessImage(input, obj);
        end
        
        if ~isa(input, 'Grasppe.ConRes.PatchGenerator.Models.ProcessImage')
          input = obj.createProcessImage([], obj);
        else
          obj.Variables = input.Variables;
        end
        
        if nargout == 0
          obj.InitializeProcess@Grasppe.Occam.Process(input);
        elseif nargout > 0
          input = obj.InitializeProcess@Grasppe.Occam.Process(input);
        end
      catch err
        debugStamp(err, 5);
      end
    end
    
    function output = TerminateProcess(obj, output)
      
      try
        if isequal(obj.Results, obj.NO_ERRORS)
          output.addProcess(obj);
        end
      catch err
        debugStamp(err,1);
        %beep;
      end
      
      %       if nargout == 0
      %         obj.TerminateProcess@Grasppe.Occam.Process(output);
      %       elseif nargout > 0
      output.Variables = obj.Variables;
      output = obj.TerminateProcess@Grasppe.Occam.Process(output);
      %       end
    end
    
    
    
    function createProcessImage(obj, image, process, mode)
      processImage = Grasppe.ConRes.PatchGenerator.Models.FourierImage;
      
      if exist('image', 'var') && ~isempty(mode)
        processImage.Image  = image;
      end
      
      if exist('process', 'var') && isa(process, 'Grasppe.Occam.Process');
        processImage.addProcess(process);
      end
      
      if exist('mode', 'var') && ~isempty(mode)
        processImage.Mode   = mode;
      end
    end
    
  end
  
end
