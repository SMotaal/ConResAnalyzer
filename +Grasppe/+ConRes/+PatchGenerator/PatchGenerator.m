classdef PatchGenerator < Grasppe.ConRes.PatchGenerator.Generator
  %PATCHGENERATOR Summary of this class goes here
  %   Detailed explanation goes here
  
  properties
    Listeners
  end
  
  methods
    function obj = PatchGenerator()
      obj.Initialize;
    end
    
    function Initialize(obj)
      % obj.CreateProcessor;
      % obj.CreateView;
      
      obj.Run;
    end
    
    function CreateView(obj)
      obj.View = Grasppe.ConRes.PatchGenerator.PatchGeneratorPanel;
      
      changeListener  = addlistener(obj.View, 'ParametersChanged', @(src, event) obj.UpdateParameters(src, event));
      applyListener   = addlistener(obj.View, 'ParametersApplied', @(src, event) obj.ApplyParameters(src, event));
      
      Listeners.ParametersChanged = changeListener;
      Listeners.ParametersApplied = applyListener;
      
      obj.View.Execute;
      
    end
    
    function CreateProcessor(obj)
      obj.Processor = Grasppe.ConRes.PatchGenerator.PatchGeneratorProcessor;
    end
    
    function Run(obj)
      if ~isobject(obj.Processor) || ~isvalid(obj.Processor), obj.CreateProcessor; end      
      if ~isobject(obj.View) || ~isvalid(obj.View), obj.CreateView; end
        
      obj.Processor.Parameters = obj.Parameters;
      obj.Processor.Execute;
    end
    
    function UpdateParameters(obj, source, event)
      obj.Parameters = source.getParameters;
    end
    
    function ApplyParameters(obj, source, event)
      obj.Parameters = source.getParameters;
      
      obj.Execute;
    end
    
    function delete(obj)
      try delete(obj.View);       end
      try delete(obj.Processor);  end
    end 
    
    
  end
  
end

