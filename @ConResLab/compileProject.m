%% Source Options
sourcePath    = cd;

%% Package Options
packageName   = 'ConResLab';
packageFiles  =  { ...
  'ConResPatchGenerator.m'
  '@ConResLab'
  'Common'
  'Grasppe.MatLab'
  '+Grasppe'
  'Classes'
  };

%% Export Configuration
exportRoot    = fullfile(cd, 'Export');
exportPath    = fullfile(exportRoot, packageName);
exportFile    = [exportPath '.zip'];

if ismac
  copyFile  = 'cp -f -p';
  copyDir   = 'cp -R -L -f -p';
  %trashFile = 'rm -f';
  %trashDir  = 'rm -R -f';
  %system('sudo -s');
elseif ispc
  copyCmd   = 'copy';
  error('Unsupported Platform!');
end

%% Export Functions
isDir       = @(x) exist(x,'dir')   == 7;
isFile      = @(x) exist(x,'file')  == 2;

%% Package Creation
try rmdir(exportPath, 's'); end
try delete(exportFile); end

mkdir(exportPath);

for m = 1:numel(packageFiles)
  pathStr   = fullfile(cd, packageFiles{m});
  if isFile(pathStr)
    cmd = [copyFile ' ' pathStr ' ' exportPath];
  elseif isDir(pathStr)
    cmd = [copyDir ' ' pathStr ' ' exportPath];
  end
  system(cmd); %[s,r] = 
end

zip(exportFile, exportPath);
