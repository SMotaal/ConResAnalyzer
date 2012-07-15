classdef MJLink
  %MJLINK Underlying MatLab-Java Linking
  %   Detailed explanation goes here
  
  properties (Constant)
    TargetPaths = {'/Users/daflair/Documents/Workspace/ConResAnalyzer/target/classes'};
  end
  
  methods (Access=private)
    
    function obj = MJLink ()
        Grasppe.Lure.Framework.MJLink.InitializeJava;
    end
    
  end
  
  
  methods (Static)
    function [instance class] = GetInstance()
      import Grasppe.Lure.Framework.*;

      persistent Instance;
      
      Class = eval(CLASS);
%             
      Instance = eval(Grasppe.Kit.GetInstance); ...
        instance = Instance; ...
        class    = Class;

%       if isempty(Instance)
%         Instance = MJLink;
%       end
%       
%        instance = Instance; ...
%          class    = Class;
      
      if nargout == 0
        assignin('caller', 'Instance', Instance);
      end
      
      %eval([Class '.AddJavaPath(instance.JavaPath)']);
    end
    
    function check = IsInitialized(value)
      persistent initialized;
      
      % check = initialized;
      
      if nargout==1
        check = isequal(initialized, true);
      end
      
      if nargin==1        
        initialized = value;
      end
      
    end
    
    function InitializeJava()
      import Grasppe.Lure.Framework.*;
      inted = MJLink.IsInitialized;
      if MJLink.IsInitialized(true)==true, return; end;
      
      % mjLink;
      
      s = warning('off', 'all');
      
      MJLink.BuildClasses();
      MJLink.ResetJava();
      MJLink.AddJavaPath(MJLink.JavaClassPath);
      MJLink.AddJavaPath(MJLink.TargetPaths);
      
      warning(s);
    end
    
    function AddJavaPath(path)
      if ischar(path), path = {path}; end
      
      javaaddpath(path{:})
    end
    
    function ResetJava()
      evalin('base', 'clear java;');
      % evalin('base', 'cleardebug;');
    end
    
    function BuildClasses()
      import Grasppe.Lure.Framework.*;
      
      % mjLink;
      
      sourcePath  = MJLink.JavaSourcePath;
      targetPath  = MJLink.JavaClassPath;
      targetPaths = MJLink.TargetPaths;
      
      files = dir(fullfile(sourcePath, '*.java'));
      
      for m = 1:numel(files)
        file = files(m);
        filename = file.name;
        
        [status, result] = MJLink.BuildClass(filename, sourcePath, targetPath, targetPaths);
      end
    end
    
    function [status, result] = BuildClass(sourcefile, sourcepath, targetpath, targetpaths)
      
      sourcefile = fullfile(sourcepath, sourcefile);
      
      [pathstr filename ext] = fileparts(sourcefile);
      
      mkdir(targetpath);
      
      delete(fullfile(targetpath, [filename '*.*']));
      
      % try
      classpath = '';
      for thispath = targetpaths
        thispath = char(thispath);
        classpath = [classpath '; ' thispath];
      end
      try classpath = classpath(2:end); end
      % end
      
      buildCommand = ['javac -sourcepath ' sourcepath ' -classpath ' classpath ' -d ' targetpath ' -g -nowarn ' sourcefile];
      
      disp(buildCommand);
      
      % keyboard;
      
      [status, result] = system(buildCommand, '-echo');
      
      % dispf('Compile
    end
    
    function pathstr = JavaPath
      [pathstr, name, ext] = fileparts(eval(FILE));
      
      pathstr = fullfile(pathstr, 'Java');
    end
    
    function pathstr = JavaClassPath
      Class = eval(CLASS);
      pathstr = fullfile(eval([Class '.JavaPath']), 'target');
    end
    
    function pathstr = JavaSourcePath
      Class = eval(CLASS);
      pathstr = fullfile(eval([Class '.JavaPath']), 'src');
    end
  end
  
end

