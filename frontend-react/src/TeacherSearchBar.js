import { useState } from 'react';
import { ReactComponent as SearchBtn } from './imgs/search.svg';
import { useHistory } from 'react-router-dom';

const TeacherSearchBar = () => {
    const [pattern, setPattern] = useState('');
    const history = useHistory();
    const [searchType, setSearchType] = useState('name');
    const [inputType, setInputType] = useState('text');

    const handleSubmit = (e) => {
        if (pattern.length > 0) {
            e.preventDefault();
            history.push(`/search/${searchType.toLowerCase()}/${pattern}`);
        }
        else {
            e.preventDefault();
            history.push('/');
        }
    }

    return (
        <div className="search-bar" id='teacher-search-bar'>
            <form onSubmit={handleSubmit}>
                <select 
                    required 
                    value={searchType} 
                    onChange={(e) => {
                        setSearchType(e.target.value);
                        if (e.target.value.toLowerCase() === 'id') {
                            setInputType('number');
                        }
                        else {
                            setInputType('text');
                        }
                    }}>
                    <option value='name'>Name</option>
                    <option value='ID'>ID</option>
                </select>
                <input 
                    type={inputType} 
                    value={pattern} 
                    placeholder={`Enter teacher ${searchType} to search`} 
                    onChange={(e) => setPattern(e.target.value)}/>
                <button><SearchBtn transform='scale(1.25)' onClick={handleSubmit}/></button>
            </form>
        </div>
    );
}
 
export default TeacherSearchBar;