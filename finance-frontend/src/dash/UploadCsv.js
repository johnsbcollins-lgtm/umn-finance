import React, { useState } from 'react';
import API_URL from '../config';
import { authHeadersForFormData } from '../config';

function UploadCsv({ onUpload }){
    const [file, setFile] = useState(null);

    function handleFileChange(e) {
    setFile(e.target.files[0]);
    }

    function handleUpload() {
        const formData = new FormData();
        formData.append('file', file);

    fetch(`${API_URL}/expenses/upload`, {
        method: 'POST',
        body: formData,
        headers: authHeadersForFormData()
    })
    .then(response => response.text())
    .then(message => {alert(message); onUpload();})
    .catch(error => console.error(error));
    }
    return (
    <div>
      <h2>Upload Wells Fargo CSV</h2>
      <input
        type="file"
        accept=".csv"
        onChange={handleFileChange}
      />
      <button onClick={handleUpload}>Upload</button>
    </div>
  );
}

export default UploadCsv;