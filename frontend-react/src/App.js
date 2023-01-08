import Navbar from './Navbar';
import { useState } from 'react';
import CourseHome from './Home';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import CourseSearchBar from './CourseSearchBar';
import AddBtn from './AddBtn';
import CourseSearchResult from './CourseSearchResult';
import AddCourse from './AddCourse';
import UpdateCourse from './UpdateCourse';
import Login from './Login';
import { useHistory } from 'react-router-dom';

const App = () => {
  const [loggedIn, setLoggedIn] = useState("not");
  const [username, setUsername] = useState("");
  const history = useHistory();

  const logOut = () => {
    setLoggedIn("not");
    history.push('/');
  }

  const logIn = (role, username) => {
    setLoggedIn(role);
    setUsername(username);
    history.push('/');
  }

  if (loggedIn === "not") {
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
    }
    else if (loggedIn == )
  }
}

export default App;
