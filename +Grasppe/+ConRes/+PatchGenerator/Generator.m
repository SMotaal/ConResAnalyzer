classdef Generator < Grasppe.ConRes.PatchGenerator.Processors.Process
  %PATCHGENERATOR Summary of this class goes here
  %   Detailed explanation goes here
  
  properties
    View
    Processor
  end
  
  methods (Abstract)
    CreateView(obj)
    CreateProcessor(obj)
  end
  
end

