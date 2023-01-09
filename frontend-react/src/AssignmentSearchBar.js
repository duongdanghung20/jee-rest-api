import { useState } from 'react';
import { ReactComponent as SearchBtn } from './imgs/search.svg';
import { useHistory } from 'react-router-dom';

const AssignmentSearchBar = () => {
    const [pattern, setPattern] = useState('');
    const history = useHistory();
    const [searchType, setSearchType] = useState('teacherId');

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
        <div className="search-bar" id='assignment-search-bar'>
            <form onSubmit={handleSubmit}>
                <select 
                    required 
                    value={searchType} 
                    onChange={(e) => {
                        setSearchType(e.target.value);
                    }}>
                    <option value='teacherId'>Teacher ID</option>
                    <option value='courseId'>Course ID</option>
                </select>
                <input 
                    type='number'
                    value={pattern} 
                    placeholder={`Enter ${searchType} to search`} 
                    onChange={(e) => setPattern(e.target.value)}/>
                <button><SearchBtn transform='scale(1.25)' onClick={handleSubmit}/></button>
            </form>
        </div>
    );
}
 
export default AssignmentSearchBar;