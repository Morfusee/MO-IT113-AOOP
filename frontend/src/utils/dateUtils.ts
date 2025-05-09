export const calculateAge = (birthday: Date) => {
  const ageDiff = Date.now() - birthday.getTime();
  const ageDate = new Date(ageDiff);
  return Math.abs(ageDate.getUTCFullYear() - 1970);
};

export const dateToString = (date: Date) => {
  return date.toISOString().split("T")[0];
};

export const stringToDate = (date: string) => {
  return new Date(date).toLocaleDateString();
};

export const stringToMonthString = (date: string) => {
  return new Date(date).toLocaleString("default", {
    month: "long",
  });
};

export const getLastDayOfMonth = (date: string) => {
  const [year, month] = date.split("-").map(Number);

  // Create a date object for the next month, then subtract one day
  const lastDay = new Date(year, month, 0).getDate();

  return `${year}-${String(month).padStart(2, "0")}-${lastDay}`;
};
