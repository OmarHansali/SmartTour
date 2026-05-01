import 'package:flutter/material.dart';
import '../models/user.dart';
import '../services/api_service.dart';

class ProfilePage extends StatefulWidget {
  const ProfilePage({super.key});

  @override
  State<ProfilePage> createState() => _ProfilePageState();
}

class _ProfilePageState extends State<ProfilePage> {
  User? _user;
  List<Favorite> _favorites = [];
  List<VisitHistory> _history = [];
  bool _isLoading = true;
  String? _error;

  @override
  void initState() {
    super.initState();
    _loadProfile();
  }

  Future<void> _loadProfile() async {
    try {
      final user = await ApiService.getProfile();
      final favorites = await ApiService.getFavorites();
      final history = await ApiService.getVisitHistory();

      setState(() {
        _user = user;
        _favorites = favorites;
        _history = history;
        _isLoading = false;
      });
    } catch (e) {
      setState(() {
        _error = 'Failed to load profile: $e';
        _isLoading = false;
      });
    }
  }

  Future<void> _removeFavorite(String placeId) async {
    try {
      await ApiService.removeFavorite(placeId);
      setState(() {
        _favorites.removeWhere((f) => f.place.id == placeId);
      });
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Removed from favorites')),
      );
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Error: $e')),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: _isLoading
          ? const Center(child: CircularProgressIndicator())
          : _error != null
              ? Center(
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      const Icon(Icons.error_outline, size: 48, color: Colors.grey),
                      const SizedBox(height: 16),
                      Text(_error!),
                      const SizedBox(height: 16),
                      FilledButton(
                        onPressed: _loadProfile,
                        child: const Text('Retry'),
                      ),
                    ],
                  ),
                )
              : CustomScrollView(
                  slivers: [
                    // Profile Header
                    SliverToBoxAdapter(
                      child: _buildProfileHeader(),
                    ),

                    // Stats
                    SliverToBoxAdapter(
                      child: _buildStatsRow(),
                    ),

                    // Preferences
                    if (_user?.preferences.isNotEmpty == true)
                      SliverToBoxAdapter(
                        child: _buildPreferencesSection(),
                      ),

                    // Favorites
                    SliverToBoxAdapter(
                      child: _buildSectionTitle('Favorite Places', Icons.favorite),
                    ),
                    _favorites.isEmpty
                        ? SliverToBoxAdapter(
                            child: _buildEmptyState('No favorites yet'),
                          )
                        : SliverList(
                            delegate: SliverChildBuilderDelegate(
                              (context, index) {
                                final favorite = _favorites[index];
                                return _buildFavoriteTile(favorite);
                              },
                              childCount: _favorites.length,
                            ),
                          ),

                    // History
                    SliverToBoxAdapter(
                      child: _buildSectionTitle('Visit History', Icons.history),
                    ),
                    _history.isEmpty
                        ? SliverToBoxAdapter(
                            child: _buildEmptyState('No visits recorded yet'),
                          )
                        : SliverList(
                            delegate: SliverChildBuilderDelegate(
                              (context, index) {
                                final visit = _history[index];
                                return _buildHistoryTile(visit);
                              },
                              childCount: _history.length,
                            ),
                          ),

                    const SliverPadding(padding: EdgeInsets.only(bottom: 32)),
                  ],
                ),
    );
  }

  Widget _buildProfileHeader() {
    return Container(
      padding: const EdgeInsets.fromLTRB(24, 64, 24, 24),
      decoration: BoxDecoration(
        gradient: LinearGradient(
          begin: Alignment.topLeft,
          end: Alignment.bottomRight,
          colors: [
            Theme.of(context).colorScheme.primary,
            Theme.of(context).colorScheme.primaryContainer,
          ],
        ),
      ),
      child: Column(
        children: [
          CircleAvatar(
            radius: 50,
            backgroundColor: Colors.white,
            backgroundImage: _user?.avatarUrl != null
                ? NetworkImage(_user!.avatarUrl!)
                : null,
            child: _user?.avatarUrl == null
                ? const Icon(Icons.person, size: 50, color: Colors.grey)
                : null,
          ),
          const SizedBox(height: 16),
          Text(
            _user?.name ?? 'User',
            style: const TextStyle(
              fontSize: 24,
              fontWeight: FontWeight.bold,
              color: Colors.white,
            ),
          ),
          const SizedBox(height: 4),
          Text(
            _user?.email ?? '',
            style: TextStyle(
              fontSize: 14,
              color: Colors.white.withOpacity(0.8),
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildStatsRow() {
    return Padding(
      padding: const EdgeInsets.all(16),
      child: Row(
        children: [
          Expanded(
            child: _buildStatCard(
              icon: Icons.place,
              value: '${_user?.totalVisits ?? 0}',
              label: 'Visits',
              color: Colors.blue,
            ),
          ),
          const SizedBox(width: 12),
          Expanded(
            child: _buildStatCard(
              icon: Icons.favorite,
              value: '${_user?.totalFavorites ?? 0}',
              label: 'Favorites',
              color: Colors.red,
            ),
          ),
          const SizedBox(width: 12),
          Expanded(
            child: _buildStatCard(
              icon: Icons.route,
              value: '${_history.length}',
              label: 'Trips',
              color: Colors.green,
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildStatCard({
    required IconData icon,
    required String value,
    required String label,
    required Color color,
  }) {
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          children: [
            Icon(icon, color: color),
            const SizedBox(height: 8),
            Text(
              value,
              style: const TextStyle(
                fontSize: 20,
                fontWeight: FontWeight.bold,
              ),
            ),
            Text(
              label,
              style: TextStyle(
                fontSize: 12,
                color: Colors.grey[600],
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildPreferencesSection() {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            'Interests',
            style: Theme.of(context).textTheme.titleSmall,
          ),
          const SizedBox(height: 8),
          Wrap(
            spacing: 8,
            runSpacing: 8,
            children: _user!.preferences.map((pref) {
              return Chip(
                label: Text(pref),
                backgroundColor: Theme.of(context).colorScheme.secondaryContainer,
              );
            }).toList(),
          ),
        ],
      ),
    );
  }

  Widget _buildSectionTitle(String title, IconData icon) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(16, 24, 16, 8),
      child: Row(
        children: [
          Icon(icon, size: 20, color: Theme.of(context).colorScheme.primary),
          const SizedBox(width: 8),
          Text(
            title,
            style: Theme.of(context).textTheme.titleMedium?.copyWith(
                  fontWeight: FontWeight.bold,
                ),
          ),
        ],
      ),
    );
  }

  Widget _buildEmptyState(String message) {
    return Padding(
      padding: const EdgeInsets.all(32),
      child: Center(
        child: Column(
          children: [
            Icon(Icons.inbox, size: 48, color: Colors.grey[400]),
            const SizedBox(height: 8),
            Text(
              message,
              style: TextStyle(color: Colors.grey[600]),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildFavoriteTile(Favorite favorite) {
    return ListTile(
      leading: CircleAvatar(
        backgroundColor: Theme.of(context).colorScheme.primaryContainer,
        child: const Icon(Icons.place),
      ),
      title: Text(favorite.place.name),
      subtitle: Row(
        children: [
          if (favorite.place.category != null)
            Text(favorite.place.category!),
          if (favorite.place.rating != null) ...[
            const SizedBox(width: 8),
            Icon(Icons.star, size: 14, color: Colors.amber[700]),
            Text(favorite.place.rating!.toStringAsFixed(1)),
          ],
        ],
      ),
      trailing: IconButton(
        icon: const Icon(Icons.delete_outline, color: Colors.red),
        onPressed: () => _removeFavorite(favorite.place.id ?? ''),
      ),
    );
  }

  Widget _buildHistoryTile(VisitHistory visit) {
    return ListTile(
      leading: CircleAvatar(
        backgroundColor: Colors.green[100],
        child: const Icon(Icons.check_circle, color: Colors.green),
      ),
      title: Text(visit.place.name),
      subtitle: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            _formatDate(visit.visitedAt),
            style: TextStyle(fontSize: 12, color: Colors.grey[600]),
          ),
          if (visit.notes != null)
            Text(
              visit.notes!,
              style: TextStyle(fontSize: 12, color: Colors.grey[500]),
              maxLines: 1,
              overflow: TextOverflow.ellipsis,
            ),
        ],
      ),
      trailing: visit.userRating != null
          ? Row(
              mainAxisSize: MainAxisSize.min,
              children: [
                Icon(Icons.star, size: 16, color: Colors.amber[700]),
                Text(visit.userRating!.toStringAsFixed(1)),
              ],
            )
          : null,
    );
  }

  String _formatDate(DateTime date) {
    final now = DateTime.now();
    final diff = now.difference(date);

    if (diff.inDays == 0) {
      if (diff.inHours == 0) {
        return '${diff.inMinutes} minutes ago';
      }
      return '${diff.inHours} hours ago';
    } else if (diff.inDays == 1) {
      return 'Yesterday';
    } else if (diff.inDays < 7) {
      return '${diff.inDays} days ago';
    } else {
      return '${date.day}/${date.month}/${date.year}';
    }
  }
}
