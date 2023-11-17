export function extractTasks(projects) {
  const tasks = [];

  projects.forEach((project) => {
    if (project.tasks && Array.isArray(project.tasks)) {
      project.tasks.forEach((task) => {
        tasks.push(task);
      });
    }
  });

  return tasks;
}

export function calculateUserPoints(extractedTasks) {
  const userPoints = {};
  const completeTasks = {};

  extractedTasks.forEach((task) => {
    const reward = task.reward;
    task.solves.forEach((solve) => {
      const username = solve.username;
      let accept = false;
      //console.log(
        // typeof solve.accept,
        // "==================",
        // typeof solve.status
      // );
      if (solve.accept === true || solve.accept === false) {
        accept = solve.accept;
      }
      if (solve.status === true || solve.status === false) {
        accept = solve.status;
      }


      if (accept) {
        if (userPoints[username]) {
          userPoints[username] += reward;
        } else {
          userPoints[username] = reward;
        }
        if (completeTasks[username]) {
          completeTasks[username]++;
        } else {
          completeTasks[username] = 1;
        }
      }
    });
  });

  return { userPoints, completeTasks };
}

export const hasValues = (data) => {
  if (data == undefined || data == null || !data) return false; // Return false for null and undefined

  return Object.values(data).some((obj) => obj && Object.keys(obj).length > 0);
};

export const sortObjectToArray = (obj) => {
  return Object.entries(obj).sort((a, b) => a[1] - b[1]);
};
