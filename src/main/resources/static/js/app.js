/**
 * efraude - Client-side JavaScript
 */

// Initialize HTMX event listeners
document.addEventListener('DOMContentLoaded', function() {
    console.log('efraude loaded');

    // Handle HTMX request errors
    document.body.addEventListener('htmx:responseError', function(event) {
        console.error('HTMX request failed:', event.detail);
        alert('An error occurred. Please try again.');
    });

    // Handle HTMX after swap (for vote buttons)
    document.body.addEventListener('htmx:afterSwap', function(event) {
        if (event.detail.target.classList.contains('vote-buttons')) {
            // Reload page to update score
            setTimeout(() => {
                window.location.reload();
            }, 500);
        }
    });

    // Form validation
    const forms = document.querySelectorAll('form[data-validate]');
    forms.forEach(form => {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        });
    });

    // Auto-dismiss alerts after 5 seconds
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.opacity = '0';
            setTimeout(() => {
                alert.remove();
            }, 300);
        }, 5000);
    });
});
