export const getFilenameFromContentDisposition = (
  defaultFileName: string,
  dispositionHeader?: string | null
): string => {
  if (!dispositionHeader) {
    return defaultFileName;
  }

  const match = dispositionHeader.match(/filename="?([^"]+)"?/);
  return match && match[1] ? match[1] : defaultFileName;
};

export const downloadFile = (url: string, filename: string): void => {
  const link = document.createElement("a");
  link.href = url;
  link.setAttribute("download", filename);
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
};
