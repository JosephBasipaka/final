import React from 'react';

function PopupMessage({ show, message, onClose ,actionType}) {
  return (
    show && (
      <div className="fixed inset-0 flex items-center justify-center z-50">
        <div className="bg-white rounded-lg p-8 shadow-lg">
          <p>{message}</p>
          <button
            onClick={() => onClose()} 
            className="mt-4 bg-indigo-700 text-white px-4 py-2 rounded-md hover:bg-indigo-500"
          >
            OK
          </button>
        </div>
      </div>
    )
  );
}

export default PopupMessage;
