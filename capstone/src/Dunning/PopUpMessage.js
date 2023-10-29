/**
 * The PopupMessage component is a React component that displays a message in a popup and provides an
 * OK button to close the popup.
 * @returns The PopupMessage component is returning a div that is conditionally rendered based on the
 * value of the "show" prop. If "show" is true, the div is rendered with a background color of white,
 * rounded corners, padding, and a shadow. Inside the div, there is a paragraph element displaying the
 * "message" prop and a button element with the text "OK".
 */
import React from "react";

function PopupMessage({ show, message, onClose, actionType }) {
  return (
    show && (
      <div className="fixed inset-0 flex items-center justify-center z-50">
        <div className="bg-white rounded-lg p-8 shadow-lg">
          <p dangerouslySetInnerHTML={{ __html: message }}></p>
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
