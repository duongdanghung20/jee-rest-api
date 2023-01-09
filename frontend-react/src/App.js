import Navbar from './Navbar';
import { useState } from 'react';
import CourseHome from './CourseHome';
import TeacherHome from './TeacherHome';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import CourseSearchBar from './CourseSearchBar';
import AddBtn from './AddBtn';
import CourseSearchResult from './CourseSearchResult';
import AddCourse from './AddCourse';
import UpdateCourse from './UpdateCourse';
import Login from './Login';
import { useHistory } from 'react-router-dom';
import TeacherSearchBar from './TeacherSearchBar';
import TeacherSearchResult from './TeacherSearchResult';
import AddTeacher from './AddTeacher';
import UpdateTeacher from './UpdateTeacher';
import AssignmentHome from './AssignmentHome';
import UpdateAssignment from './UpdateAssignment';
import useFetch from './useFetch';
import AssignmentSearchBar from './AssignmentSearchBar';
import AssignmentSearchResult from './AssignmentSearchResult';

const App = () => {
  const [loggedIn, setLoggedIn] = useState(sessionStorage.getItem("loggedIn"));
  const [username, setUsername] = useState(sessionStorage.getItem("username"));
  const history = useHistory();

  const logOut = () => {
    setLoggedIn("not");
    setUsername("");
    sessionStorage.setItem("loggedIn", "not");
    sessionStorage.setItem("username", "");
    history.push('/');
  }

  const logIn = (role, username) => {
    setLoggedIn(role);
    sessionStorage.setItem("loggedIn", role);
    setUsername(username);
    sessionStorage.setItem("username", username);
    history.push('/');
  }

  if (loggedIn === "not" || loggedIn === null || loggedIn === undefined) {
    return (
      <div className="App">
        <div className="Content">
          <Login logIn={logIn}/>
        </div>
      </div>
    );
  }
  else {
    if (loggedIn === "scolarite") {
      return (
        <Router>
          <div className="App">
          <Navbar logOut={logOut} username={username}/>
            <div className="Content">
              <Switch>
                <Route exact path='/'>
                  <h1>Course List</h1>
                  <CourseSearchBar />
                  <AddBtn />
                  <CourseHome />
                </Route>
                <Route path='/search/:searchtype/:pattern'>
                  <h1>Course List</h1>
                  <CourseSearchBar />
                  <AddBtn />
                  <CourseSearchResult />
                </Route>
                <Route path='/add'>
                  <AddCourse />
                </Route>
                <Route path='/update/:courseId/:courseName/:courseSemester/:courseNumCMHours/:courseNumTDHours/:courseNumTPHours/:courseNumStudents/:courseThresholdCM/:courseThresholdTD/:courseThresholdTP'>
                  <UpdateCourse />
                </Route>
              </Switch>
            </div>
          </div>
        </Router>
      );
    } else if (loggedIn === "rh") {
      return (
        <Router>
          <div className="App">
          <Navbar logOut={logOut} username={username}/>
            <div className="Content">
              <Switch>
                <Route exact path='/'>
                  <h1>Teacher List</h1>
                  <TeacherSearchBar />
                  <AddBtn />
                  <TeacherHome />
                </Route>
                <Route path='/search/:searchtype/:pattern'>
                  <h1>Teacher List</h1>
                  <TeacherSearchBar />
                  <AddBtn />
                  <TeacherSearchResult />
                </Route>
                <Route path='/add'>
                  <AddTeacher />
                </Route>
                <Route path='/update/:teacherId/:teacherFirstName/:teacherLastName/:teacherNumHours/:teacherEq/:teacherDept/:teacherService/:teacherNumDisHours/:teacherMaxOvtHours'>
                  <UpdateTeacher />
                </Route>
              </Switch>
            </div>
          </div>
        </Router>
      );
    } else if (loggedIn === "gdd") {
      return (
        <Router>
          <div className="App">
          <Navbar logOut={logOut} username={username}/>
            <div className="Content">
              <Switch>
                <Route exact path='/'>
                  <h1>Assignment List</h1>
                  <AssignmentSearchBar />
                  <AssignmentHome />
                </Route>
                <Route path='/search/:searchtype/:pattern'>
                  <AssignmentSearchResult />
                </Route>
                <Route path='/update/:assignmentId/:teacherId/'>
                  <UpdateAssignment />
                </Route>
              </Switch>
            </div>
          </div>
        </Router>
      );
    }
  }
}

export default App;
