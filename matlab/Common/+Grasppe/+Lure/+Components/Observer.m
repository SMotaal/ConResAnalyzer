classdef Observer < Grasppe.Lure.Framework.JWrapper
  %OBSERVER Summary of this class goes here
  %   Detailed explanation goes here
  
  properties
    javaObserver
  end
  
  methods
    
    function obj = Observer()
      mjLink;
      
      obj.javaObserver = com.grasppe.lure.components.Facade;
    end
  end
  
end

